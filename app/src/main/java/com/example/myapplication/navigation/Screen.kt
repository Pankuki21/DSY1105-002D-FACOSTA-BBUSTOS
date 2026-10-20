package com.example.myapplication.navigation

sealed class Screen(
    val route: String
) {

    data object Home : Screen("home")

    data object RegistrarDepartamento :
        Screen("registrar_departamento")

    data object RegistrarCliente : Screen("registrar_cliente")

    data object Propiedades : Screen("propiedades")

    data object Clientes : Screen("clientes")

    data object Resumen :
        Screen("resumen")

    data object Profile :
        Screen("profile")

    data object Settings :
        Screen("settings")
}
