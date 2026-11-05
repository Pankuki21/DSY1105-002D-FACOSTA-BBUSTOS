package com.example.myapplication.repository

import com.example.myapplication.data.model.SolicitudApiDto
import com.example.myapplication.data.remote.SolicitudesApiService
import com.example.myapplication.model.ClientePotencialRemoto
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk

class ClientesPotencialesRepositoryTest : FunSpec({
    test("convierte respuestas remotas en referencias del dominio PropTrack") {
        val servicio = mockk<SolicitudesApiService>()
        coEvery { servicio.obtenerSolicitudes() } returns listOf(
            SolicitudApiDto(
                id = 42,
                userId = 7,
                title = "Preferencia de arriendo",
                body = "Busca un departamento con dos dormitorios"
            )
        )

        val clientes = ClientesPotencialesRepository(servicio).obtenerClientesPotenciales()

        clientes shouldContainExactly listOf(
            ClientePotencialRemoto(
                solicitudId = 42,
                referenciaCliente = 7,
                largoDescripcion = "Busca un departamento con dos dormitorios".length
            )
        )
    }
})
