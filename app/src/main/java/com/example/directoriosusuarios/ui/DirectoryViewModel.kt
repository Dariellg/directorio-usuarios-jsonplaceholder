package com.example.directoriosusuarios.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.directoriosusuarios.data.local.FavoriteEntity
import com.example.directoriosusuarios.data.model.Address
import com.example.directoriosusuarios.data.model.Company
import com.example.directoriosusuarios.data.model.Post
import com.example.directoriosusuarios.data.model.User
import com.example.directoriosusuarios.data.repository.RepositoryResult
import com.example.directoriosusuarios.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoadState<out T> { data object Loading : LoadState<Nothing>; data class Success<T>(val data: T) : LoadState<T>; data class Error(val message: String) : LoadState<Nothing> }
data class DirectoryUiState(val users: LoadState<List<User>> = LoadState.Loading, val favorites: List<FavoriteEntity> = emptyList(), val posts: LoadState<List<Post>> = LoadState.Loading)

class DirectoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(DirectoryUiState())
    val state: StateFlow<DirectoryUiState> = _state.asStateFlow()

    init { observeFavorites(); loadUsers() }

    private fun observeFavorites() = viewModelScope.launch { repository.observeFavorites().collect { favs -> _state.update { it.copy(favorites = favs) } } }

    fun loadUsers() = viewModelScope.launch {
        _state.update { it.copy(users = LoadState.Loading) }
        _state.update { it.copy(users = when (val result = repository.getUsers()) { is RepositoryResult.Success -> LoadState.Success(result.value); is RepositoryResult.Error -> LoadState.Error(result.message) }) }
    }

    fun loadPosts(userId: Int) = viewModelScope.launch {
        _state.update { it.copy(posts = LoadState.Loading) }
        _state.update { it.copy(posts = when (val result = repository.getPosts(userId)) { is RepositoryResult.Success -> LoadState.Success(result.value); is RepositoryResult.Error -> LoadState.Error(result.message) }) }
    }

    fun toggleFavorite(user: User) = viewModelScope.launch {
        val exists = _state.value.favorites.any { it.id == user.id }
        repository.toggleFavorite(user, exists)
    }

    fun favoriteAsUser(entity: FavoriteEntity) = User(entity.id, entity.name, entity.username, entity.email, entity.phone, entity.website, Address(entity.street, entity.suite, entity.city, entity.zipcode), Company(entity.company, "", ""))
}
