package com.example.nav_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.nav_room.data.AppDatabase
import com.example.nav_room.data.NoteRepository
import com.example.nav_room.navigation.AppNavGraph
import com.example.nav_room.ui.NoteViewModel
import com.example.nav_room.ui.theme.Nav_RoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "note_database"
        ).build()

        val repository = NoteRepository(database.noteDao())
        val viewModel = NoteViewModel(repository)

        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController = navController, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Nav_RoomTheme {
        Greeting("Android")
    }
}

@Composable
fun Greeting(s: String) {

}
