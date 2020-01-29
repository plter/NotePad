package com.plter.notepad.sectors;

import com.plter.lib.java.sector.Sector;

public class RootSector extends Sector {

	
	public static final String NAME="RootSector";
	
	private RootSector(){
		
		super(NAME);
		
		//添加NOTE数据部门
		addSector(new NotesDataSector());
		
		//添加文件浏览部门
		addSector(new BrowseFilesSector());
	}
	
	private static RootSector __RootSector=null;
	
	public static RootSector getRootSector(){
		if (__RootSector==null) {
			__RootSector=new RootSector();
		}
		return __RootSector;
	}
	
	@Override
	public RootSectorManager getManager() {
		return (RootSectorManager) super.getManager();
	}
}
