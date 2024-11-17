package com.example.nav_room.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nav_room.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Note)

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?
}