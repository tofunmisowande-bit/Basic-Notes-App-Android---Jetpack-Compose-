package com.example.fullnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val time: String,
    val date: String,
)
