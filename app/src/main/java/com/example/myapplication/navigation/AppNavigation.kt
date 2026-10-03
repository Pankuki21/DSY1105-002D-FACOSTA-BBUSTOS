package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.screens.NavigationHomeScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.RegistroScreen
import com.example.myapplication.ui.screens.ResumenScreen
import com.example.myapplication.ui.screens.SettingsScreen
import com.example.myapplication.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // Pantallas de la Guía 10
        composable(route = Screen.Home.route) {
            NavigationHomeScreen(
                onNavigate = { screen ->
                    navController.navigate(screen.route)
                }
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onNavigate = { screen ->
                    navController.navigate(screen.route)
                }
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onNavigate = { screen ->
                    navController.navigate(screen.route)
                }
            )
        }

        // Pantallas de la Guía 11
        composable(route = Screen.Registro.route) {
            RegistroScreen(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }

        composable(route = Screen.Resumen.route) {
            ResumenScreen(
                usuarioViewModel = usuarioViewModel
            )
        }
    }
}