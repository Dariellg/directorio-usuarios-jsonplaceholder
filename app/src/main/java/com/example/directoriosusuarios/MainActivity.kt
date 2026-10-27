package com.example.directoriosusuarios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.directoriosusuarios.ui.DirectoryApp
import com.example.directoriosusuarios.ui.DirectoryViewModel
import com.example.directoriosusuarios.ui.theme.DirectoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as DirectoryApplication).repository
        setContent {
            DirectoryTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DirectoryApp(viewModel = viewModel { DirectoryViewModel(repository) })
                }
            }
        }
    }
}
