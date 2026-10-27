package com.example.directoriosusuarios.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val company: String,
    val username: String,
    val phone: String,
    val website: String,
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String
)
