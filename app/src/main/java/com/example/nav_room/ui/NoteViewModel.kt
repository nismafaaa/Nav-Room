package com.example.nav_room.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nav_room.data.Note
import com.example.nav_room.data.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val allNotes: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch {
            repository.update(note)
        }
    }

    suspend fun getNoteById(id: Int): Note? {
        return repository.getNoteById(id)
    }


}