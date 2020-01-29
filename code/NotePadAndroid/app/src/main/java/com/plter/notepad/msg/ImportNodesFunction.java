package com.plter.notepad.msg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.plter.lib.java.sector.Function;
import com.plter.lib.java.sector.protocols.ICommand;
import com.plter.notepad.sectors.NotesDataSector;

public class ImportNodesFunction extends Function {

	public ImportNodesFunction() {
		super(Commands.IMPORT_NOTES);
	}
	
	
	@Override
	public boolean execute(ICommand command) {
		
		File f= (File) command.getContent();
		try {
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			String line=null;
			String content="";
			while((line=br.readLine())!=null){
				content+=line;
			}
			br.close();
			JSONObject obj = new JSONObject(content);
			return getSector().importNotesJson(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	@Override
	public NotesDataSector getSector() {
		return (NotesDataSector) super.getSector();
	}

}
