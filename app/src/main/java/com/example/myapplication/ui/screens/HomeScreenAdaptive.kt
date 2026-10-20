package com.example.myapplication.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun HomeScreenAdaptive(
    widthSizeClass: WindowWidthSizeClass,
    onRegistrarDepartamento: () -> Unit
) {
    when (widthSizeClass) {

        WindowWidthSizeClass.Compact -> {
            HomeScreenCompacta(
                onRegistrarDepartamento = onRegistrarDepartamento
            )
        }

        WindowWidthSizeClass.Medium -> {
            HomeScreenMediana(
                onRegistrarDepartamento = onRegistrarDepartamento
            )
        }

        WindowWidthSizeClass.Expanded -> {
            HomeScreenExpanded(
                onRegistrarDepartamento = onRegistrarDepartamento
            )
        }
    }
}