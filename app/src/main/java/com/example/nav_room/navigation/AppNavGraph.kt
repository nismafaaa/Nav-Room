package com.example.nav_room.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nav_room.ui.NoteViewModel
import com.example.nav_room.ui.screens.DetailScreen
import com.example.nav_room.ui.screens.HomeScreen

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: NoteViewModel) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) }
        composable("detail/{noteId}", arguments = listOf(navArgument("noteId"){nullable = true})) {backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            DetailScreen(navController, viewModel, noteId)
        }
        composable("detail") {
            DetailScreen(navController, viewModel, noteId = null)
        }
    }
}