package com.plter.notepad.vcs;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.plter.lib.android.java.anim.AnimationStyle;
import com.plter.lib.android.java.controls.ViewController;
import com.plter.lib.android.java.controls.ViewControllerEvent;
import com.plter.lib.java.event.EventListener;
import com.plter.lib.java.utils.TimeUtil;
import com.plter.notepad.data.NotesProvider;
import com.plter.notepad.sectors.RootSector;
import com.topyunp.notepadandroid.R;

public class AddNoteVC extends ViewController {


    private EditText titleTxt, contentTxt;
    private Button saveBtn, saveAsBtn, cancelBtn;

    public AddNoteVC(Context context) {
        super(context, R.layout.add_note_layout);

        setAnimationStyle(AnimationStyle.FLIP_HORIZONTAL);

        titleTxt = (EditText) findViewByID(R.id.titleTxt);
        contentTxt = (EditText) findViewByID(R.id.contentTxt);
        saveBtn = (Button) findViewByID(R.id.saveBtn);
        saveAsBtn = (Button) findViewByID(R.id.saveAsBtn);
        cancelBtn = (Button) findViewByID(R.id.cancelBtn);

        saveBtn.setOnClickListener(btnOnClickListener);
        saveAsBtn.setOnClickListener(btnOnClickListener);
        cancelBtn.setOnClickListener(btnOnClickListener);

        backing.add(backingListener);
    }

    private EventListener<ViewControllerEvent> backingListener = new EventListener<ViewControllerEvent>() {
        @Override
        public boolean onReceive(Object target, ViewControllerEvent event) {
            save(true);
            return true;
        }
    };


    private final View.OnClickListener btnOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.saveBtn:
                    save(false);
                    break;
                case R.id.saveAsBtn:
                    cloneNote(false);
                    break;
                case R.id.cancelBtn:
                    getPrevious().forcePopViewController(true);
                    break;
                default:
                    break;
            }
        }
    };


    public String getTitle() {
        return titleTxt.getText().toString();
    }


    public void setTitle(String title) {
        titleTxt.setText(title);
    }


    /**
     * 保存日志
     *
     * @param silentMode 是否为安静模式，如果为安静模式，则不显示任何提示
     */
    public void save(boolean silentMode) {
        if (getId() <= -1) {
            cloneNote(silentMode);
            return;
        }

        ContentValues cv = checkTextAndMakeContentValues(silentMode);
        if (cv != null) {
            getContext().getContentResolver().update(NotesProvider.URI, cv, NotesProvider._ID + " = " + getId(), null);

            RootSector.getRootSector().dispatchEvent(com.plter.notepad.msg.Events.NOTE_DATA_CHANGED);
            getPrevious().forcePopViewController(true);
        }
    }


    /**
     * 是否为安静模式
     *
     * @param silentMode 是否为安静模式，如果是安静模式，则不显示任何提示
     */
    public void cloneNote(boolean silentMode) {
        ContentValues cv = checkTextAndMakeContentValues(silentMode);

        if (cv != null) {
            getContext().getContentResolver().insert(NotesProvider.URI, cv);

            RootSector.getRootSector().dispatchEvent(com.plter.notepad.msg.Events.NOTE_DATA_CHANGED);
            getPrevious().forcePopViewController(true);
        }
    }


    /**
     * 返回值可能为null
     *
     * @param silentMode 是否为安静模式，如果是安静模式，则不显示任何提示
     * @return
     */
    private ContentValues checkTextAndMakeContentValues(boolean silentMode) {
        String title = getTitle();
        if (title.equals("")) {
            if (!silentMode) {
                Toast.makeText(getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        String content = getContent();
        if (content.equals("")) {
            if (!silentMode) {
                Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        ContentValues cv = new ContentValues();
        cv.put(NotesProvider.TITLE, title);
        cv.put(NotesProvider.CONTENT, content);
        cv.put(NotesProvider.MODIFIED_DATE, "修改日期：" + TimeUtil.getCurrentTimeString());
        return cv;
    }


    public String getContent() {
        return contentTxt.getText().toString();
    }


    public void setContent(String content) {
        contentTxt.setText(content);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id = -1;


    public void reset() {
        setId(-1);
        setTitle("");
        setContent("");
    }
}
