package com.example.myapplication.model

data class ClientesPotencialesUiState(
    val cargando: Boolean = true,
    val clientes: List<ClientePotencialRemoto> = emptyList(),
    val mensajeError: String? = null
)
