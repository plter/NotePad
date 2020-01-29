package com.plter.notepad.msg;

import com.plter.lib.android.java.anim.AnimationStyle;
import com.plter.lib.android.java.controls.IViewController;
import com.plter.lib.java.sector.Function;
import com.plter.lib.java.sector.protocols.ICommand;
import com.plter.notepad.vcs.FileBrowserVC;

public class BrowseFilesFunc extends Function {

	public BrowseFilesFunc() {
		super(Commands.BROWSE_FILES);
	}
	
	
	@Override
	public boolean execute(ICommand command) {
		
		Object[] args = (Object[]) command.getContent();
		IViewController vc = (IViewController) args[0];
		String dir = (String) args[1];
		
		FileBrowserVC fbVc = new FileBrowserVC(dir);
		fbVc.setAnimationStyle(AnimationStyle.COVER_VERTICAL);
		vc.pushViewController(fbVc, true);
		return super.execute(command);
	}

}
