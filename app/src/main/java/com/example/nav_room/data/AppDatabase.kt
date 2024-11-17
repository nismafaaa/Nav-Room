package com.example.nav_room.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nav_room.data.NoteDao
import com.example.nav_room.data.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}