package com.plter.notepad.sectors;

import com.plter.lib.java.sector.Manager;
import com.plter.lib.java.sector.protocols.IEvent;
import com.plter.notepad.NotePadActivity;
import com.plter.notepad.msg.Events;

public class RootSectorManager extends Manager {

	public RootSectorManager(Object component) {
		super(component);
	}
	
	@Override
	public NotePadActivity getComponent() {
		return (NotePadActivity) super.getComponent();
	}
	
	
	@Override
	public boolean handleEvent(IEvent event) {
		if (event.getName().equals(Events.NOTE_DATA_CHANGED)) {
			getComponent().refreshNoteList();
		}
		return super.handleEvent(event);
	}
}
