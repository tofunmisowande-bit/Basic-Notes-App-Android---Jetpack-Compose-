package com.example.fullnotes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.fullnotes.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao{

    @Upsert
    suspend fun insertNote(notes: Note)

    @Delete
    suspend fun deleteNote(notes: Note)

    @Query("SELECT * FROM note where id = :id")
    suspend fun getNotesById(id:Int): Note?

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

}