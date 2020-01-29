package com.plter.notepad.msg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import com.plter.lib.java.sector.Function;
import com.plter.lib.java.sector.protocols.ICommand;
import com.plter.notepad.sectors.NotesDataSector;

public class ExportNodesFunction extends Function {

	public ExportNodesFunction() {
		super(Commands.EXPORT_NOTES);
	}

	@Override
	public boolean execute(ICommand command) {		
		
		JSONObject json = getSector().getNotesJson();
		File f = new File((String) command.getContent());
		if (!f.exists()) {
			try {
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(json.toString().getBytes("utf-8"));
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public NotesDataSector getSector() {
		return (NotesDataSector) super.getSector();
	}
}
