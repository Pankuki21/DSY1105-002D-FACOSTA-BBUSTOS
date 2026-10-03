package com.example.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.myapplication.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreenAdaptive() {

    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {

        WindowWidthSizeClass.Compact -> {
            HomeScreenCompacta()
        }

        WindowWidthSizeClass.Medium -> {
            HomeScreenMediana()
        }

        WindowWidthSizeClass.Expanded -> {
            HomeScreenExpanded()
        }
    }
}