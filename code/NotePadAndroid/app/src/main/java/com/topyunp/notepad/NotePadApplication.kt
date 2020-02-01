package com.topyunp.notepad

import android.app.Application
import androidx.room.Room
import com.topyunp.notepad.dal.AppDatabase

class NotePadApplication : Application() {


    private lateinit var _db: AppDatabase

    val db: AppDatabase
        get() = _db

    override fun onCreate() {
        super.onCreate()
        _db = Room.databaseBuilder(this, AppDatabase::class.java, "notes")
            .enableMultiInstanceInvalidation().build()
    }


}