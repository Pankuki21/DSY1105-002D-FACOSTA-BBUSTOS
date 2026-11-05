package com.example.myapplication.viewmodel

import com.example.myapplication.model.ClientePotencialRemoto
import com.example.myapplication.repository.ClientesPotencialesRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ClientesPotencialesViewModelTest : FunSpec({
    test("publica los clientes recibidos desde el repositorio") {
        runTest {
            Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
            try {
                val repositorio = mockk<ClientesPotencialesRepository>()
                val esperado = listOf(ClientePotencialRemoto(3, 2, 48))
                coEvery { repositorio.obtenerClientesPotenciales() } returns esperado

                val viewModel = ClientesPotencialesViewModel(repositorio)
                advanceUntilIdle()

                viewModel.estado.value.clientes shouldBe esperado
                viewModel.estado.value.cargando shouldBe false
                viewModel.estado.value.mensajeError shouldBe null
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    test("expone un mensaje recuperable cuando falla el servicio") {
        runTest {
            Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
            try {
                val repositorio = mockk<ClientesPotencialesRepository>()
                coEvery { repositorio.obtenerClientesPotenciales() } throws IllegalStateException()

                val viewModel = ClientesPotencialesViewModel(repositorio)
                advanceUntilIdle()

                viewModel.estado.value.cargando shouldBe false
                viewModel.estado.value.mensajeError shouldBe
                    "No se pudieron cargar los clientes potenciales. Revisa tu conexión e inténtalo de nuevo."
            } finally {
                Dispatchers.resetMain()
            }
        }
    }
})
