package com.example.fullnotes.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullnotes.model.Note
import com.example.fullnotes.model.NotesViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesTextScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: NotesViewModel
){
    var noteTitle by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var noteLength by remember { mutableIntStateOf(0) }


    val currentTime  = LocalDateTime.now()

    val formater = remember {
        DateTimeFormatter.ofPattern("dd MMMM H:mm")
    }

    val formattedTime = remember(currentTime){
        currentTime.format(formater).lowercase()
    }

    noteLength = noteText.length

    Column (
        modifier = modifier.fillMaxSize()
    ){
        TopAppBar(
            title = { Text(text = "") },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        viewModel.addNote(Note( title = noteTitle, text = noteText))
                        onNavigateBack()
                    }
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteTitle,
                onValueChange = { noteTitle = it },
                placeholder = {
                    Text(
                        text = "Title",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp),
                text = "$formattedTime | $noteLength characters"
            )
        }

        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            placeholder = { Text(text = "Start writing...") },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

