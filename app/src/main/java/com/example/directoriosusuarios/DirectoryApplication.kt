package com.example.directoriosusuarios

import android.app.Application
import com.example.directoriosusuarios.data.local.AppDatabase
import com.example.directoriosusuarios.data.remote.ApiClient
import com.example.directoriosusuarios.data.repository.UserRepository

class DirectoryApplication : Application() {
    val database by lazy { AppDatabase.create(this) }
    val repository by lazy { UserRepository(ApiClient.service, database.favoriteDao()) }
}
