package com.plter.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.plter.lib.android.java.controls.ViewControllerActivity;
import com.plter.lib.java.utils.TimeUtil;
import com.plter.notepad.config.Config;
import com.plter.notepad.data.NotesProvider;
import com.plter.notepad.msg.Commands;
import com.plter.notepad.sectors.RootSector;
import com.plter.notepad.sectors.RootSectorManager;
import com.plter.notepad.vcs.AddNoteVC;
import com.topyunp.notepadandroid.R;

public class NotePadActivity extends ViewControllerActivity implements OnClickListener {

    private AddNoteVC addNoteVc;
    private ListView noteListView;
    private SimpleCursorAdapter noteListViewAdapter;
    private RootSectorManager manager = null;

    public static final int DELETE_ITEM_ID = 1;
    public static final int CANCEL_ITEM_ID = 2;
    public static final int VISIT_AUTHOR_BLOG = 2;


    public NotePadActivity() {
        manager = new RootSectorManager(this);
        RootSector.getRootSector().setManager(manager);
    }


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initComponents();
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        initAddNoteVC();
        initAdapter();
        initNoteListView();
        initButtonListeners();
    }


    private void initButtonListeners() {
        findViewById(R.id.addBtn).setOnClickListener(this);
        findViewById(R.id.importBtn).setOnClickListener(this);
        findViewById(R.id.exportBtn).setOnClickListener(this);
    }

    public void viewNode(int position) {
        Cursor c = noteListViewAdapter.getCursor();
        c.moveToPosition(position);

        addNoteVc.setTitle(c.getString(c.getColumnIndex(NotesProvider.TITLE)));
        addNoteVc.setContent(c.getString(c.getColumnIndex(NotesProvider.CONTENT)));
        addNoteVc.setId(c.getInt(c.getColumnIndex(NotesProvider._ID)));

        pushViewController(addNoteVc, true);
    }

    private void initNoteListView() {
        noteListView = (ListView) findViewById(R.id.noteListView);
        noteListView.setAdapter(noteListViewAdapter);
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                viewNode(arg2);
            }
        });
        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {

                new AlertDialog.Builder(NotePadActivity.this)
                        .setTitle("操作选项")
                        .setItems(new CharSequence[]{"查看", "删除", "导入", "导出所有", "删除所有"}, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        viewNode(arg2);
                                        break;
                                    case 1:
                                        removeNoteOnPosition(arg2);
                                        break;
                                    case 2:
                                        importNotes();
                                        break;
                                    case 3:
                                        exportNotes();
                                        break;
                                    case 4:
                                        removeAllNotes();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .setNeutralButton("取消", null)
                        .show();
                return true;
            }
        });
    }


    private void initAddNoteVC() {
        addNoteVc = new AddNoteVC(this);
    }

    private void initAdapter() {
        noteListViewAdapter = new SimpleCursorAdapter(this,
                R.layout.note_list_cell_layout,
                null,
                new String[]{NotesProvider.TITLE, NotesProvider.MODIFIED_DATE},
                new int[]{R.id.titleTv, R.id.modifiedDateTv});

        refreshNoteList();
    }


    /**
     * 刷新笔记列表
     */
    public void refreshNoteList() {
        Cursor cursor = getContentResolver().query(NotesProvider.URI, null, null, null, NotesProvider._ID + " DESC");
        noteListViewAdapter.changeCursor(cursor);
    }


    /**
     * 从数据库中删除与Adapter中指定位置所对应的数据行
     *
     * @param position
     */
    public void removeNoteOnPosition(int position) {
        Cursor c = noteListViewAdapter.getCursor();
        c.moveToPosition(position);
        getContentResolver().delete(NotesProvider.URI, NotesProvider._ID + " = " + c.getInt(c.getColumnIndex(NotesProvider._ID)), null);

        refreshNoteList();
    }


    /**
     * 删除所有日志
     */
    public void removeAllNotes() {

        getContentResolver().delete(NotesProvider.URI, null, null);

        refreshNoteList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, VISIT_AUTHOR_BLOG, 0, "作者微博：http://weibo.com/plter");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case VISIT_AUTHOR_BLOG:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://weibo.com/plter"));
                startActivity(i);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 导出所有日志
     */
    public void exportNotes() {
        String path = Config.SD_CARD_PATH + TimeUtil.getCurrentTimeString("", "", "") + ".notes";
        if (getManager().sendCommand(Commands.EXPORT_NOTES, path)) {
            new AlertDialog.Builder(this)
                    .setTitle("哈哈")
                    .setMessage("已将所有日志成功导出到文件\n" + path)
                    .setPositiveButton("查看", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getManager().sendCommand(Commands.BROWSE_FILES, new Object[]{NotePadActivity.this, Config.SD_CARD_PATH});
                        }
                    })
                    .setNeutralButton("关闭", null)
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle("导出失败").setMessage("亲爱的主人，对不起，我已经很努力了，但还是失败了").show();
        }
    }


    /**
     * 导入日志
     */
    public void importNotes() {
        getManager().sendCommand(Commands.BROWSE_FILES, new Object[]{this, Config.SD_CARD_PATH});
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn:
                addNoteVc.reset();
                pushViewController(addNoteVc, true);
                break;
            case R.id.importBtn:
                importNotes();
                break;
            case R.id.exportBtn:
                exportNotes();
                break;
            default:
                break;
        }
    }


    public RootSectorManager getManager() {
        return manager;
    }
}