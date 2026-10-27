package com.example.directoriosusuarios.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY name") fun observeAll(): Flow<List<FavoriteEntity>>
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)") fun observeIsFavorite(id: Int): Flow<Boolean>
    @Upsert suspend fun upsert(favorite: FavoriteEntity)
    @Query("DELETE FROM favorites WHERE id = :id") suspend fun delete(id: Int)
}
