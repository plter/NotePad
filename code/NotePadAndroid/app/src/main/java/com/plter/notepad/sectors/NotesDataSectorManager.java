package com.plter.notepad.sectors;

import com.plter.lib.java.sector.Manager;
import com.plter.notepad.data.NotesDataObject;

public class NotesDataSectorManager extends Manager{

	public NotesDataSectorManager() {
		super(new NotesDataObject());
	}

	@Override
	public NotesDataObject getComponent() {
		return (NotesDataObject) super.getComponent();
	}
}
