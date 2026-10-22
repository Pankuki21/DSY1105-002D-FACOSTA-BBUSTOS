package com.example.myapplication.navigation

sealed class Screen(
    val route: String
) {

    // Rutas existentes de PropTrack
    data object Home : Screen("home")

    data object RegistrarDepartamento :
        Screen("registrar_departamento")

    data object RegistrarCliente :
        Screen("registrar_cliente")

    data object Propiedades :
        Screen("propiedades")

    data object Clientes :
        Screen("clientes")

    data object Resumen :
        Screen("resumen")

    data object Profile :
        Screen("profile")

    data object Settings :
        Screen("settings")

    // Rutas del módulo integral de usuarios
    data object RegistroIntegral :
        Screen("registro_integral")

    data object ResumenIntegral :
        Screen("resumen_integral")

    data object ListadoOffline :
        Screen("listado_offline")
}