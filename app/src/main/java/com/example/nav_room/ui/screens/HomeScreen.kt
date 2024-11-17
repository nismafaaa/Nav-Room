package com.example.nav_room.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nav_room.ui.NoteViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: NoteViewModel) {
    val notes by viewModel.allNotes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Spasi antar item catatan
    ) {
        Button(
            onClick = { navController.navigate("detail") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Note")
        }

        notes.forEach { note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {  // Navigasi ke layar detail saat item diklik
                        navController.navigate("detail/${note.id}")
                    },
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { viewModel.deleteNote(note) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Note",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}
