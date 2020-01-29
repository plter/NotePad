package com.plter.notepad.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class NotesProvider extends ContentProvider {

	
	public static final String TABLE_NAME=NoteSQLConnection.TABLE_NAME;
	public static final String _ID=NoteSQLConnection._ID;
	public static final String TITLE=NoteSQLConnection.TITLE;
	public static final String MODIFIED_DATE=NoteSQLConnection.MODIFIED_DATE;
	public static final String CONTENT=NoteSQLConnection.CONTENT;
	
	public static final Uri URI=Uri.parse("content://com.plter.notepad.data.noteprovider");
	
	private NoteSQLConnection conn;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return conn.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		conn.getWritableDatabase().insert(TABLE_NAME, null, values);
		return uri;
	}

	@Override
	public boolean onCreate() {
		conn=new NoteSQLConnection(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return conn.getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return conn.getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
	}
}
