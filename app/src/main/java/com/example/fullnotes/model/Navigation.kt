package com.example.fullnotes.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.fullnotes.data.EditScreen
import com.example.fullnotes.data.HomeScreen
import com.example.fullnotes.data.TextScreen
import com.example.fullnotes.ui.presentation.EditNoteScreen
import com.example.fullnotes.ui.presentation.NotesScreen
import com.example.fullnotes.ui.presentation.NotesTextScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesNavigation(
    viewModel: NotesViewModel,
    modifier: Modifier = Modifier
){

    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = HomeScreen,
        modifier = modifier
    ){

        composable<HomeScreen>{
            NotesScreen(
                viewModel = viewModel,
                onAddNote = {
                    navController.navigate(TextScreen)
                },
                navigateToEdit = {note ->
                    navController.navigate(EditScreen(note.id))
                }
            )
        }

        composable<TextScreen> {
            NotesTextScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable<EditScreen>{backStackEntry ->
            val editScreen = backStackEntry.toRoute<EditScreen>()
            EditNoteScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
                noteId = editScreen.id

            )
        }
    }
}