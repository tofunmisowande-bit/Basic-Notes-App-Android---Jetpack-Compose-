package com.example.fullnotes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.fullnotes.model.Notes
import com.example.fullnotes.ui.theme.FullNotesTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.room.Delete
import androidx.room.Room
import com.example.fullnotes.model.NotesViewModel
import java.time.LocalDate
import java.util.Locale
import kotlin.collections.emptyList
import kotlin.jvm.java


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes_db"
        ).build()

        val viewModel = NotesViewModel(db.noteDao())

        enableEdgeToEdge()
        setContent {
            FullNotesTheme {
                NotesDisplay(viewModel)
            }
        }
    }
}


@Dao
interface NotesDao{
    @Query("SELECT * FROM note")
        fun getAllNotes(): Flow<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(notes: Notes)

    @Delete
    suspend fun deleteItem(item: Notes)

}
@Database(entities = [Notes::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun noteDao() :  NotesDao
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotesContent(
    notes: List<Notes>,
    notesText: String,
    onNotesTextChange:(String) -> Unit,
    onNotesAdd: () -> Unit,
    onDelete: (Notes) -> Unit,
    onEdit: (Notes) -> Unit,
    isEditing : Boolean
){

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
                title = {Text(text = "Notes",
                    fontSize = 30.sp,
                    fontWeight = Bold,)},
            )


        if(notes.isEmpty()){
        Text(text = "No Notes yet",
            modifier = Modifier.padding(8.dp)
                .weight(1f))
    }else{
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(notes.size) { index ->
                val note = notes[index]
                Card(
                    modifier = Modifier.padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {


                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .combinedClickable(
                                onClick = {
                                    onEdit(note)
                                },
                                onLongClick = {
                                    onDelete(note)
                                }
                            )) {
                        Text(text = note.text,
                            fontWeight = Bold,
                            fontSize = 30.sp
                        )
                        Text(text = "${note.date} ${note.time}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }    }

        Row(verticalAlignment = Alignment.CenterVertically) {

            TextField(
                value = notesText,
                onValueChange = onNotesTextChange,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.DarkGray,
                    focusedContainerColor = Color.LightGray,
                    focusedTextColor = Color.Black),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)

            )
            FilledIconButton(onClick = {onNotesAdd()},
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )) {
               if(!isEditing){
                   Icon(Icons.Default.Add, contentDescription = "Add")
               }
                else{
                    Icon(Icons.Default.Edit, contentDescription =  "Edit")
               }
            }

        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesDisplay(viewModel: NotesViewModel, modifier: Modifier = Modifier){
    var notesText by remember {mutableStateOf("")  }
    var editText by remember { mutableStateOf<Notes?>(null) }
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    NotesContent(
        notes = notes,
        notesText = notesText,
        onNotesTextChange = {notesText = it},
        onNotesAdd = {
            val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d", Locale.ENGLISH))

            if (editText != null){
                if(notesText.isBlank()){
                    viewModel.deleteNote(editText!!)
                }else{
                viewModel.addNotes(Notes((editText!!.id), notesText, currentTime, currentDate))
                }
                editText = null
            }
            else{
                viewModel.addNotes(Notes(text = notesText, time = currentTime, date = currentDate))
                notesText = ""
            }
        },
        onDelete = {
            viewModel.deleteNote(it)
        },
        onEdit = {
            notesText = it.text
            editText = it
        },
        isEditing = editText != null

    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FullNotesTheme {
        val fakeNotes = listOf(
            Notes(1, "Buy food", "14:30", "May 4"),
            Notes(2, "Read book", "16:00", "May 6")
        )

        var text by remember { mutableStateOf("") }

        NotesContent(
            notes = fakeNotes,
            notesText = text,
            onNotesTextChange = { text = it },
            onNotesAdd = {},
            onDelete = {},
            onEdit = {},
            isEditing = true
        )
    }
}
