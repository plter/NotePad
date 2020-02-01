package com.topyunp.notepadandroid.dal

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY modified_date DESC")
    fun getAll(): List<Note>

    @Insert
    fun insertNotes(vararg users: Note)

    @Update
    fun updateNotes(vararg users: Note): Int

    @Delete
    fun deleteNotes(vararg users: Note): Int


}