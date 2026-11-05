package com.example.myapplication.data.model

/** Representación de la respuesta externa usada para consultar interesados. */
data class SolicitudApiDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
