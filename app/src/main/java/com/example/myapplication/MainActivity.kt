package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.PropTrackDatabase
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.repository.UsuarioRepository
import com.example.myapplication.ui.themes.MyApplicationTheme
import com.example.myapplication.viewmodel.UsuarioViewModel
import com.example.myapplication.viewmodel.UsuarioViewModelFactory
import com.example.myapplication.data.EstadoDataStore

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var usuarioViewModelFactory: UsuarioViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener la base de datos existente de PropTrack.
        val database = PropTrackDatabase.getDatabase(applicationContext)

        // Crear el repositorio de usuarios.
        usuarioRepository = UsuarioRepository(database.usuarioDao())

        // Crear la fábrica del ViewModel.
        val estadoDataStore = EstadoDataStore(applicationContext)

        usuarioViewModelFactory = UsuarioViewModelFactory(
            repository = usuarioRepository,
            estadoDataStore = estadoDataStore,
            context = applicationContext
        )

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)

            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = usuarioViewModelFactory
            )

            MyApplicationTheme {
                AppNavigation(
                    widthSizeClass = windowSizeClass.widthSizeClass,
                    usuarioViewModel = usuarioViewModel,
                    usuarioRepository = usuarioRepository
                )
            }
        }
    }
}