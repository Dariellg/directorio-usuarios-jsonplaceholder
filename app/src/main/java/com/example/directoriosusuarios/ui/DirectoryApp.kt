@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.directoriosusuarios.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.directoriosusuarios.data.local.FavoriteEntity
import com.example.directoriosusuarios.data.model.Post
import com.example.directoriosusuarios.data.model.User

@Composable
fun DirectoryApp(viewModel: DirectoryViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selected by rememberSaveable { mutableStateOf<Int?>(null) }
    var tab by rememberSaveable { mutableStateOf(0) }
    val users = (state.users as? LoadState.Success)?.data.orEmpty()
    val selectedUser = users.firstOrNull { it.id == selected } ?: state.favorites.firstOrNull { it.id == selected }?.let(viewModel::favoriteAsUser)

    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(tab == 0, { tab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Usuarios") })
            NavigationBarItem(tab == 1, { tab = 1 }, icon = { Icon(Icons.Default.Favorite, null) }, label = { Text("Favoritos") })
        }
    }) { padding ->
        if (selectedUser != null) DetailScreen(selectedUser, state, state.favorites.any { it.id == selectedUser.id }, viewModel, { selected = null }, Modifier.padding(padding))
        else if (tab == 0) UserListScreen(state, viewModel, { selected = it.id }, Modifier.padding(padding))
        else FavoritesScreen(state.favorites, { selected = it.id }, Modifier.padding(padding))
    }
}

@Composable
private fun UserListScreen(state: DirectoryUiState, viewModel: DirectoryViewModel, onSelect: (User) -> Unit, modifier: Modifier) {
    Scaffold(modifier = modifier, topBar = { TopAppBar(title = { Text("Directorio de usuarios") }) }) { inner ->
        when (val users = state.users) {
            LoadState.Loading -> LoadingContent(Modifier.padding(inner))
            is LoadState.Error -> ErrorContent(users.message, viewModel::loadUsers, Modifier.padding(inner))
            is LoadState.Success -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(inner)) { items(users.data) { UserCard(it, state.favorites.any { fav -> fav.id == it.id }, { viewModel.toggleFavorite(it) }, { onSelect(it) }) } }
        }
    }
}

@Composable
private fun FavoritesScreen(favorites: List<FavoriteEntity>, onSelect: (FavoriteEntity) -> Unit, modifier: Modifier) {
    Scaffold(modifier = modifier, topBar = { TopAppBar(title = { Text("Mis favoritos") }) }) { inner ->
        if (favorites.isEmpty()) EmptyContent("Aún no tienes favoritos", "Guarda usuarios para consultarlos sin conexión.", Modifier.padding(inner))
        else LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(inner)) { items(favorites) { favorite -> Card(Modifier.fillMaxWidth().clickable { onSelect(favorite) }) { ListItem(headlineContent = { Text(favorite.name, fontWeight = FontWeight.Bold) }, supportingContent = { Text("${favorite.email} · ${favorite.company}") }, leadingContent = { Icon(Icons.Default.Favorite, null, tint = MaterialTheme.colorScheme.primary) }) } } }
    }
}

@Composable
private fun UserCard(user: User, favorite: Boolean, onFavorite: () -> Unit, onSelect: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onSelect() }) { ListItem(headlineContent = { Text(user.name, fontWeight = FontWeight.Bold) }, supportingContent = { Column { Text(user.email); Text(user.company.name, color = MaterialTheme.colorScheme.primary) } }, trailingContent = { IconButton(onClick = onFavorite) { Icon(if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Favorito") } }) }
}

@Composable
private fun DetailScreen(user: User, state: DirectoryUiState, favorite: Boolean, viewModel: DirectoryViewModel, onBack: () -> Unit, modifier: Modifier) {
    androidx.compose.runtime.LaunchedEffect(user.id) { viewModel.loadPosts(user.id) }
    Scaffold(modifier = modifier, topBar = { TopAppBar(title = { Text(user.name) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } }, actions = { IconButton(onClick = { viewModel.toggleFavorite(user) }) { Icon(if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Favorito") } }) }) { inner ->
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(inner)) {
            item { Text("Información de contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            item { InfoRow(Icons.Default.Email, user.email); InfoRow(Icons.Default.Phone, user.phone); InfoRow(Icons.Default.Language, user.website); InfoRow(Icons.Default.Home, "${user.address.street}, ${user.address.suite}, ${user.address.city}"); InfoRow(Icons.Default.Business, user.company.name) }
            item { Text("Publicaciones", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            when (val posts = state.posts) { LoadState.Loading -> item { LoadingContent(Modifier.fillMaxWidth()) }; is LoadState.Error -> item { ErrorContent(posts.message, { viewModel.loadPosts(user.id) }, Modifier.fillMaxWidth()) }; is LoadState.Success -> items(posts.data) { PostCard(it) } }
        }
    }
}

@Composable private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) { Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) { Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(10.dp)); Text(text) } }
@Composable private fun PostCard(post: Post) { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp)) { Text(post.title.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold); Spacer(Modifier.height(6.dp)); Text(post.body) } } }
@Composable private fun LoadingContent(modifier: Modifier) { Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator(); Spacer(Modifier.height(12.dp)); Text("Cargando información…") } } }
@Composable private fun ErrorContent(message: String, retry: () -> Unit, modifier: Modifier) { Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(44.dp)); Spacer(Modifier.height(10.dp)); Text(message, textAlign = androidx.compose.ui.text.style.TextAlign.Center); Spacer(Modifier.height(16.dp)); Button(onClick = retry) { Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text("Reintentar") } } } }
@Composable private fun EmptyContent(title: String, message: String, modifier: Modifier) { Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(48.dp)); Spacer(Modifier.height(12.dp)); Text(title, style = MaterialTheme.typography.titleMedium); Text(message) } } }
