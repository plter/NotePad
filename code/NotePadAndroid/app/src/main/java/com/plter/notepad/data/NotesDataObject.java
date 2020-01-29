package com.plter.notepad.data;

import com.plter.notepad.sectors.RootSector;

import android.content.Context;

public class NotesDataObject {

	
	public NotesDataObject() {
		this.context=RootSector.getRootSector().getManager().getComponent();
	}
	
	
	public Context getContext() {
		return context;
	}
	
	private Context context=null;
}
