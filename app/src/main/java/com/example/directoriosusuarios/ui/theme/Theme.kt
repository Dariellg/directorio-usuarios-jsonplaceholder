package com.example.directoriosusuarios.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = lightColorScheme(primary = Color(0xFF315A92), secondary = Color(0xFF5C5F71), tertiary = Color(0xFF76546C))

@Composable fun DirectoryTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = Colors, content = content) }
