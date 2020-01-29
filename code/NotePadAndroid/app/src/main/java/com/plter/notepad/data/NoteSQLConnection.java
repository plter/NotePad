package com.plter.notepad.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteSQLConnection extends SQLiteOpenHelper {
	
	public static final String TABLE_NAME="notepad";
	public static final String _ID="_id";
	public static final String TITLE="title";
	public static final String MODIFIED_DATE="modified_date";
	public static final String CONTENT="content";
	
	public NoteSQLConnection(Context context) {
		super(context, "notepad.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
				_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				TITLE+" TEXT NOT NULL DEFAULT \"\"," +
				MODIFIED_DATE+" TEXT NOT NULL DEFAULT \"\"," +
				CONTENT+" TEXT NOT NULL DEFAULT \"\")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
