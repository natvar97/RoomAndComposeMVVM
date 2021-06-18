package com.example.roomandcompose.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomandcompose.utils.NoteApplication
import kotlinx.coroutines.launch

class NoteViewModel(
    application: Application
) : AndroidViewModel(application) {


    val allNotes : LiveData<List<Note>>
    private val noteRepository : NoteRepository

    init {
        val noteDao = NoteDatabase.getInstance(application).noteDao()
        noteRepository = NoteRepository(noteDao)
        allNotes = noteRepository.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        noteRepository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        noteRepository.delete(note)
    }

}