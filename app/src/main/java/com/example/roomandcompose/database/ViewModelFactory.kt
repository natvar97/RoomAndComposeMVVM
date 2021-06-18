package com.example.roomandcompose.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomandcompose.utils.NoteApplication
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown View Model Class found!!")
    }
}