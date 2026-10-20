package com.example.myapplication.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun NavigationHomeScreen(
    widthSizeClass: WindowWidthSizeClass,
    onRegistrarDepartamento: () -> Unit
) {

    HomeScreen(
        widthSizeClass = widthSizeClass,
        onRegistrarDepartamento = onRegistrarDepartamento
    )
}