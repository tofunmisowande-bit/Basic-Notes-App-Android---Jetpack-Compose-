package com.example.fullnotes.model

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fullnotes.data.NotesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val notesDao: NotesDao): ViewModel() {
    val notes = notesDao.getAllNotes()

    private val _currentNote = MutableStateFlow<Note?>(null)

    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    fun addNote(note: Note){
         viewModelScope.launch {
             notesDao.insertNote(note)
         }
    }

    fun saveNote(id: Int, title: String, text: String) {
        viewModelScope.launch {
            if (id == -1) {
                notesDao.insertNote(Note(title = title, text = text))
            } else {
                notesDao.insertNote(Note(id = id, title = title, text = text))
            }
        }
    }

    fun loadById(id: Int){
        viewModelScope.launch {
            _currentNote.value = notesDao.getNotesById(id)
        }
    }

    suspend fun getById(id: Int): Note?{
        return notesDao.getNotesById(id)
    }


    fun deleteNote(note:Note){
        viewModelScope.launch {
            notesDao.deleteNote(note)
        }
    }



}