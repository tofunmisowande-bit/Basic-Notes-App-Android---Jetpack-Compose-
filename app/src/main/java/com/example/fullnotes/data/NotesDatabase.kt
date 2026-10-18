package com.example.fullnotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fullnotes.model.Note


@Database(entities = [Note::class], version = 2)
abstract class NotesDatabase: RoomDatabase(){
    abstract fun notesDao(): NotesDao
}
