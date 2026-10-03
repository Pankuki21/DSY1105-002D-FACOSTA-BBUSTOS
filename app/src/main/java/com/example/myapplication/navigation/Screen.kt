package com.example.myapplication.navigation

sealed class Screen(val route: String) {

    data object Home : Screen(route = "home_page")

    data object Profile : Screen(route = "profile_page")

    data object Settings : Screen(route = "settings_page")

    data object Registro : Screen(route = "registro")

    data object Resumen : Screen(route = "resumen")

    data class Detail(
        val itemId: String
    ) : Screen(route = "detail_page/{itemId}") {

        fun buildRoute(): String {
            return route.replace(
                oldValue = "{itemId}",
                newValue = itemId
            )
        }
    }
}