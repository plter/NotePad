package com.plter.notepad.sectors;

import com.plter.lib.java.sector.Sector;
import com.plter.notepad.msg.BrowseFilesFunc;

public class BrowseFilesSector extends Sector {

	
	public static final String NAME="FileBrowserSector";
	
	public BrowseFilesSector() {
		super(NAME);
		
		addFunction(new BrowseFilesFunc());
	}
	
	

}
