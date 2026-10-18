package com.example.fullnotes.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullnotes.model.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesCard(
    modifier: Modifier = Modifier,
    note: Note,
    onDeleteNote: (Note) -> Unit,
    navigateToEdit: (Note) -> Unit
){

    var isRevealed by remember { mutableStateOf(false) }





    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(note.id){
                detectTapGestures (
                    onTap = {},
                    onLongPress = {
                        isRevealed = !isRevealed
                    }
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        )
    ) {

        val currentTime  = LocalDateTime.now()

        val formater =
        if (currentTime.hour < 12){
            remember {
            DateTimeFormatter.ofPattern("H:mm")
        }
        } else{
            remember {
                DateTimeFormatter.ofPattern("MMMM dd")
            }
        }

        val formattedTime = remember(currentTime){
            currentTime.format(formater).lowercase()
        }


        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .animateContentSize()
            ) {
                Text(
                    text = note.title,
                    fontWeight = Bold,
                    fontSize = 30.sp,
                    maxLines = 1
                )
                Spacer(Modifier.size(2.dp))
                Text(
                    text = note.text,
                    maxLines = if(isRevealed) 2 else 4
                )
                Spacer(Modifier.size(2.dp))
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
            Spacer(Modifier.weight(1f))

            AnimatedVisibility(
                visible = isRevealed,
                modifier = Modifier
                    .padding(8.dp),
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    IconButton(
                        onClick = {
                            navigateToEdit(note)
                            isRevealed = false
                        }
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(
                        onClick = {
                            onDeleteNote(note)
                            isRevealed = false
                        }
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteList(
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList(),
    navigateToEdit: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit
){

    Column(modifier = modifier) {
        if (notes.isEmpty()) {
            Text(
                text = "No Notes yet",
                modifier = Modifier.padding(8.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = notes,
                    key = { it.id }) { note ->
                    NotesCard(
                        note = note,
                        modifier = Modifier.padding(8.dp),
                        navigateToEdit = {navigateToEdit(note)},
                        onDeleteNote = {onDeleteNote(note)}
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun NoteCardPreview(){
    NotesCard(
        note = Note(
            title = "Buy Food",
            text = "Buy some food for dinner",
        ),
        onDeleteNote = {},
        navigateToEdit = {},
    )
}