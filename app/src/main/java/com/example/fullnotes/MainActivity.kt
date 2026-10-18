package com.example.fullnotes


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import com.example.fullnotes.data.NotesDatabase
import com.example.fullnotes.model.NotesNavigation
import com.example.fullnotes.model.NotesViewModel
import com.example.fullnotes.ui.theme.FullNotesTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val db = Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes_db"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

       // val viewModel: NotesViewModel by viewModels(
          //  factoryProducer = { NotesViewModel.Factory(db.notesDao()) }
      //  )

        val viewModel  = NotesViewModel(db.notesDao())

        enableEdgeToEdge()
        setContent {
            FullNotesTheme {
                Scaffold { innerPadding ->
                    NotesNavigation(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}