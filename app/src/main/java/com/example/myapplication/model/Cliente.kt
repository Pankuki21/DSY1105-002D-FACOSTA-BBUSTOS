package com.example.myapplication.model

data class Cliente(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val tipoOperacion: TipoOperacion,
    val rentaMensual: Double,
    val presupuesto: Double,
    val preferencias: String
)