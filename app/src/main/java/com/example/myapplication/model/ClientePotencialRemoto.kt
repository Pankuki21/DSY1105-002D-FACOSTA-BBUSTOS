package com.example.myapplication.model

/** Datos seguros para presentar una respuesta remota dentro del flujo de clientes. */
data class ClientePotencialRemoto(
    val solicitudId: Int,
    val referenciaCliente: Int,
    val largoDescripcion: Int
)
