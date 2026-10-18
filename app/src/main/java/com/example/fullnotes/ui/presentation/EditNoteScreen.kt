package com.example.fullnotes.ui.presentation



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullnotes.data.NotesDao
import com.example.fullnotes.model.Note
import com.example.fullnotes.model.NotesViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    noteId: Int,
    onNavigateBack: () -> Unit,
    viewModel: NotesViewModel,

){
    val currentNote by viewModel.currentNote.collectAsState()
    Text(
        text = "ID: $noteId | Note: ${currentNote?.title}"
    )

    LaunchedEffect(noteId) {
            if(noteId != -1){
            viewModel.loadById(noteId)
        }
    }

    if(noteId != -1 && currentNote == null){
        Box(modifier = Modifier.fillMaxSize())
    }
    else{
        EditNote(
            noteId = noteId,
            onNavigateBack = onNavigateBack,
            initialTitle = if(noteId == -1) "" else currentNote?.title.orEmpty(),
            initialText = if(noteId == -1) "" else currentNote?.text.orEmpty(),
            onSave = {title , text ->
                viewModel.saveNote(id= noteId, title= title, text = text)
            }
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditNote(
    modifier: Modifier = Modifier,
    noteId: Int,
    onNavigateBack: () -> Unit,
    initialTitle: String,
    initialText: String,
    onSave: (String, String) -> Unit
) {
    var noteTitle by remember { mutableStateOf(initialTitle) }
    var noteText by remember { mutableStateOf(initialText) }
    var noteLength by remember { mutableIntStateOf(0) }

    val currentTime = LocalDateTime.now()

    val formater = remember {
        DateTimeFormatter.ofPattern("dd MMMM H:mm")
    }

    val formattedTime = remember(currentTime) {
        currentTime.format(formater).lowercase()
    }

    noteLength = noteText.length

    LaunchedEffect(initialTitle, initialText) {
        noteTitle = initialTitle
        noteText = initialText
    }


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
                        onSave(noteTitle, noteText)
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


