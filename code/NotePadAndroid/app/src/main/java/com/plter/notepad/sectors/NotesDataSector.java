package com.plter.notepad.sectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.plter.lib.java.sector.Sector;
import com.plter.lib.java.utils.TimeUtil;
import com.plter.notepad.data.NotesProvider;
import com.plter.notepad.msg.Events;
import com.plter.notepad.msg.ExportNodesFunction;
import com.plter.notepad.msg.ImportNodesFunction;

public class NotesDataSector extends Sector {
	
	public static final String NAME="NotesDataSector";
	
	
	public NotesDataSector() {
		super(NAME);
		
		addFunction(new ExportNodesFunction());
		addFunction(new ImportNodesFunction());
	}
	
	
	public ContentResolver getContentResolver(){
		return RootSector.getRootSector().getManager().getComponent().getContentResolver();
	}
	
	public Cursor getNotesDataCursor(){
		return getContentResolver().query(NotesProvider.URI, null, null, null, null);
	}
	
	
	public boolean importNotesJson(JSONObject obj){
		
		try {
			JSONArray notesArr = obj.getJSONArray("notes");
			JSONObject note=null;
			
			for (int i = 0; i < notesArr.length(); i++) {
				ContentValues cv = new ContentValues();
				note=notesArr.getJSONObject(i);
				cv.put(NotesProvider.TITLE, note.getString(NotesProvider.TITLE));
				cv.put(NotesProvider.MODIFIED_DATE, note.getString(NotesProvider.MODIFIED_DATE));
				cv.put(NotesProvider.CONTENT, note.getString(NotesProvider.CONTENT));
				
				getContentResolver().insert(NotesProvider.URI, cv);
			}
			
			getRoot().dispatchEvent(Events.NOTE_DATA_CHANGED);
			
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public JSONObject getNotesJson(){
		JSONObject jsonObj = null;
		try {
			jsonObj=new JSONObject();
			jsonObj.put("backupDate", TimeUtil.getCurrentTimeString());
			
			JSONArray notesArr = new JSONArray();
			JSONObject note;
			Cursor cursor = getNotesDataCursor();
			while(cursor.moveToNext()){
				note=new JSONObject();
				note.put(NotesProvider._ID, cursor.getInt(cursor.getColumnIndex(NotesProvider._ID)));
				note.put(NotesProvider.CONTENT, cursor.getString(cursor.getColumnIndex(NotesProvider.CONTENT)));
				note.put(NotesProvider.MODIFIED_DATE, cursor.getString(cursor.getColumnIndex(NotesProvider.MODIFIED_DATE)));
				note.put(NotesProvider.TITLE, cursor.getString(cursor.getColumnIndex(NotesProvider.TITLE)));
				
				notesArr.put(note);
			}
			
			jsonObj.put("notes", notesArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
