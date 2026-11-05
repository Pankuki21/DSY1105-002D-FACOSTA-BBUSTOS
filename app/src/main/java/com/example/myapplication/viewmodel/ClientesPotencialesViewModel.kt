package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ClientesPotencialesUiState
import com.example.myapplication.repository.ClientesPotencialesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientesPotencialesViewModel(
    private val repositorio: ClientesPotencialesRepository
) : ViewModel() {
    private val _estado = MutableStateFlow(ClientesPotencialesUiState())
    val estado = _estado.asStateFlow()

    init {
        cargarClientes()
    }

    fun cargarClientes() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, mensajeError = null) }
            try {
                val clientes = repositorio.obtenerClientesPotenciales()
                _estado.value = ClientesPotencialesUiState(
                    cargando = false,
                    clientes = clientes
                )
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _estado.update {
                    it.copy(
                        cargando = false,
                        mensajeError = "No se pudieron cargar los clientes potenciales. Revisa tu conexión e inténtalo de nuevo."
                    )
                }
            }
        }
    }
}
