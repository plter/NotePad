package com.topyunp.notepadandroid.dal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Note::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}