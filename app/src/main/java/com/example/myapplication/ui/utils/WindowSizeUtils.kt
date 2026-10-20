package com.example.myapplication.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

fun esPantallaCompacta(
    widthSizeClass: WindowWidthSizeClass
): Boolean {
    return widthSizeClass == WindowWidthSizeClass.Compact
}

fun esPantallaMediana(
    widthSizeClass: WindowWidthSizeClass
): Boolean {
    return widthSizeClass == WindowWidthSizeClass.Medium
}

fun esPantallaExpandida(
    widthSizeClass: WindowWidthSizeClass
): Boolean {
    return widthSizeClass == WindowWidthSizeClass.Expanded
}