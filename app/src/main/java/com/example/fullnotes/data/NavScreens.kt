package com.example.fullnotes.data

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object TextScreen

@Serializable
data class EditScreen(val id: Int)