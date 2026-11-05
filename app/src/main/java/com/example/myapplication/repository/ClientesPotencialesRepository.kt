package com.example.myapplication.repository

import com.example.myapplication.data.remote.SolicitudesApiService
import com.example.myapplication.model.ClientePotencialRemoto

class ClientesPotencialesRepository(
    private val servicio: SolicitudesApiService
) {
    suspend fun obtenerClientesPotenciales(): List<ClientePotencialRemoto> =
        servicio.obtenerSolicitudes().map { solicitud ->
            ClientePotencialRemoto(
                solicitudId = solicitud.id,
                referenciaCliente = solicitud.userId,
                largoDescripcion = solicitud.body.length
            )
        }
}
