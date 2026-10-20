package com.example.myapplication.model

data class Departamento(
    val id: Int,
    val numero: String,
    val piso: Int,
    val metrosCuadrados: Double,
    val dormitorios: Int,
    val banos: Int,
    val terraza: Boolean,
    val precio: Double,
    val tipoOperacion: TipoOperacion,
    val estado: EstadoDepartamento
)

enum class TipoOperacion {
    ARRIENDO,
    COMPRA
}

enum class EstadoDepartamento {
    DISPONIBLE,
    RESERVADO,
    VENDIDO,
    ARRENDADO
}