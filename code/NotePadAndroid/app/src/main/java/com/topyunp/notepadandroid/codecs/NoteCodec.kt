package com.topyunp.notepadandroid.codecs

import com.topyunp.notepadandroid.dal.Note
import com.topyunp.notepadandroid.dal.NoteFieldNames
import com.topyunp.notepadandroid.helpers.DateHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object NoteCodec {
    fun encode(notes: List<Note>): JSONObject? {
        var jsonObj: JSONObject? = null

        try {
            jsonObj = JSONObject()
            jsonObj.put("backupDate", DateHelper.getCurrentTimeString())
            val notesArr = JSONArray()
            var note: JSONObject
            notes.forEach {
                note = JSONObject()
                note.put(NoteFieldNames._ID, it.nid)
                note.put(
                    NoteFieldNames.CONTENT,
                    it.content
                )
                note.put(
                    NoteFieldNames.MODIFIED_DATE,
                    it.modifiedDate
                )
                note.put(
                    NoteFieldNames.TITLE,
                    it.title
                )
                notesArr.put(note)
            }
            jsonObj.put("notes", notesArr)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObj
    }

    fun decode(notesJsonString: String): JSONObject? {
        return try {
            JSONObject(notesJsonString)
        } catch (e: JSONException) {
            null
        }
    }
}