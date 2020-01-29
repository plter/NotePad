package com.plter.notepad.vcs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plter.lib.android.java.anim.AnimationStyle;
import com.plter.lib.android.java.controls.ArrayAdapter;
import com.plter.lib.android.java.controls.ListViewController;
import com.plter.notepad.config.Config;
import com.plter.notepad.msg.Commands;
import com.plter.notepad.sectors.RootSector;
import com.topyunp.notepadandroid.R;

import java.io.File;

public class FileBrowserVC extends ListViewController implements OnItemClickListener, OnItemLongClickListener {

	public FileBrowserVC(String dirPath) {
		super(RootSector.getRootSector().getManager().getComponent(), R.layout.file_browser_list);

		setAnimationStyle(AnimationStyle.PUSH_RIGHT_TO_LEFT);

		adapter=new FileBrowserListAdapter(getContext());

		File dir = new File(dirPath);

		File[] files=dir.listFiles();
		if (files!=null) {
			adapter.setFileArr(files);
		}

		setAdapter(adapter);

		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final FileBrowserListCellData data = adapter.getItem(arg2);
		if (data.getFile().isDirectory()) {
			pushViewController(new FileBrowserVC(data.getFile().getAbsolutePath()), true);
		}else if(data.isNotes()){
			new AlertDialog.Builder(getContext())
			.setTitle("请问")
			.setMessage("您要导入该文件吗？")
			.setPositiveButton("是的", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(RootSector.getRootSector().sendCommand(Commands.IMPORT_NOTES, data.getFile())){
						getRootViewController().popViewController(true);
					}else{
						Toast.makeText(getContext(), "导入文件失败", Toast.LENGTH_SHORT).show();
					}

				}
			})
			.setNegativeButton("取消", null)
			.show();

		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {

		final FileBrowserListCellData data = adapter.getItem(position);
		if (data.isNotes()) {

			//TODO 呈现上下文菜单
			new AlertDialog.Builder(getContext())
			.setTitle("操作选项")
			.setItems(new CharSequence[]{"导入","删除"}, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					switch (which) {
					case 0:
						if(RootSector.getRootSector().sendCommand(Commands.IMPORT_NOTES, data.getFile())){
							getRootViewController().popViewController(true);
						}else{
							Toast.makeText(getContext(), "导入文件失败", Toast.LENGTH_SHORT).show();
						}
						break;
					case 1:
						data.getFile().delete();
						adapter.remove(position);
						break;
					}
				}
			})
			.setNeutralButton("取消", null)
			.show();

			return true;
		}

		return false;
	}

	private FileBrowserListAdapter adapter;


	//FileBrowserListAdapter>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public static class FileBrowserListAdapter extends ArrayAdapter<FileBrowserListCellData>{

		public FileBrowserListAdapter(Context context) {
			super(context, R.layout.file_browser_list_cell);
		}


		public void setFileArr(File[] files){
			for (File file : files) {
				if (file.getName().charAt(0)!='.') {
					add(new FileBrowserListCellData(file));
				}
			}
		}

		@Override
		public void initListCell(int position, View listCell, ViewGroup parent) {
			FileBrowserListCellData data = getItem(position);

			TextView nameTv=(TextView) listCell.findViewById(R.id.fileNameTv);
			nameTv.setText(data.getName());

			ImageView dirRightIv = (ImageView) listCell.findViewById(R.id.dirRightIconIv);
			if (data.getFile().isDirectory()) {
				dirRightIv.setImageResource(R.drawable.dir_right);
			}else{
				dirRightIv.setImageBitmap(null);
			}

			ImageView iconIv= (ImageView) listCell.findViewById(R.id.fileIconIv);
			iconIv.setImageResource(data.getIconRes());
		}

	}
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//FileBrowserListCellData>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public static class FileBrowserListCellData{

		public FileBrowserListCellData(File f) {
			file=f;

			if (f.isDirectory()) {
				iconRes=R.drawable.dir_icon;
			}else{
				if (getName().indexOf('.')>-1) {
					String fileType = getName().substring(getName().lastIndexOf('.')).toLowerCase();
					if (fileType.equals(Config.NOTES_FILE_TYPE)) {
						iconRes=R.drawable.app_icon;
					}else{
						iconRes=R.drawable.empty_file_icon;
					}
				}else{
					iconRes=R.drawable.empty_file_icon;						
				}
			}
		}


		public String getName(){
			return getFile().getName();
		}


		public File getFile() {
			return file;
		}



		private int iconRes=0;
		public int getIconRes() {
			return iconRes;
		}


		/**
		 * 判断该条数据内所包含的文件是否为日志备份文件
		 * @return
		 */
		public boolean isNotes(){
			return getFile().isDirectory()?false:getFile().getName().indexOf('.')>-1?getFile().getName().substring(getFile().getName().lastIndexOf('.')).toLowerCase().equals(Config.NOTES_FILE_TYPE):false;
		}



		private File file=null;
	}
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

}
