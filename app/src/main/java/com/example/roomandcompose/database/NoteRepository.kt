package com.example.roomandcompose.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class NoteRepository(
    private val noteDao: NoteDao
) {

    var allNotes: LiveData<List<Note>> = noteDao.getNotes()

    @WorkerThread
    suspend fun insert(note: Note) = noteDao.insert(note)

    @WorkerThread
    suspend fun update(note: Note) = noteDao.update(note)

    @WorkerThread
    suspend fun delete(note: Note) = noteDao.delete(note)

}