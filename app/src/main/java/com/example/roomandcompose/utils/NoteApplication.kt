package com.example.roomandcompose.utils

import android.app.Application
import com.example.roomandcompose.database.NoteDatabase
import com.example.roomandcompose.database.NoteRepository

class NoteApplication : Application() {

    private val noteDatabase by lazy {
        NoteDatabase.getInstance(this@NoteApplication)
    }

    val repository by lazy {
        NoteRepository(noteDatabase.noteDao())
    }

}