package com.example.directoriosusuarios.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    companion object { fun create(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "directory.db").build() }
}
