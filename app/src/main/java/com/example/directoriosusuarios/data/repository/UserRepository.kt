package com.example.directoriosusuarios.data.repository

import com.example.directoriosusuarios.data.local.FavoriteDao
import com.example.directoriosusuarios.data.local.FavoriteEntity
import com.example.directoriosusuarios.data.model.User
import com.example.directoriosusuarios.data.remote.UsersApi
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

sealed interface RepositoryResult<out T> { data class Success<T>(val value: T): RepositoryResult<T>; data class Error(val message: String): RepositoryResult<Nothing> }

class UserRepository(private val api: UsersApi, private val dao: FavoriteDao) {
    fun observeFavorites() = dao.observeAll()
    fun observeIsFavorite(id: Int): Flow<Boolean> = dao.observeIsFavorite(id)

    suspend fun getUsers() = safeCall { api.getUsers().bodyOrThrow() }
    suspend fun getPosts(userId: Int) = safeCall { api.getPosts(userId).bodyOrThrow() }
    suspend fun toggleFavorite(user: User, isFavorite: Boolean) {
        if (isFavorite) dao.delete(user.id) else dao.upsert(user.toEntity())
    }

    private suspend fun <T> safeCall(block: suspend () -> T): RepositoryResult<T> = try {
        RepositoryResult.Success(block())
    } catch (_: IOException) { RepositoryResult.Error("Sin conexión a Internet. Revisa tu red e inténtalo de nuevo.")
    } catch (e: HttpException) { RepositoryResult.Error("Error HTTP ${e.code()}. Inténtalo de nuevo.")
    } catch (_: Exception) { RepositoryResult.Error("Ocurrió un error inesperado. Inténtalo de nuevo.") }

    private fun User.toEntity() = FavoriteEntity(id, name, email, company.name, username, phone, website, address.street, address.suite, address.city, address.zipcode)
    private fun <T> retrofit2.Response<T>.bodyOrThrow(): T = if (isSuccessful) body() ?: error("Respuesta vacía") else throw HttpException(this)
}
