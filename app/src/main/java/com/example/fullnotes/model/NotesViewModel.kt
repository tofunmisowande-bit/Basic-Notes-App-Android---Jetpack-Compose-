package com.example.fullnotes.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fullnotes.NotesDao
import kotlinx.coroutines.launch

class NotesViewModel(private val notesDao: NotesDao): ViewModel() {
    val notes = notesDao.getAllNotes()

    fun addNotes(note: Notes){
        viewModelScope.launch{
            notesDao.insertNote(note)
        }
    }

    fun deleteNote(note:Notes){
        viewModelScope.launch {
            notesDao.deleteItem(note)
        }
    }
}