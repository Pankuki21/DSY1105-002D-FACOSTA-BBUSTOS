package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.EstadoDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EstadoViewModel(application: Application) :
    AndroidViewModel(application) {

    private val almacenamiento = EstadoDataStore(application)

    private val _modoActivado = MutableStateFlow(false)
    val modoActivado: StateFlow<Boolean> =
        _modoActivado.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> =
        _cargando.asStateFlow()

    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> =
        _guardado.asStateFlow()

    init {
        viewModelScope.launch {
            almacenamiento.obtenerEstado()
                .catch {
                    _cargando.value = false
                }
                .collect { estado ->
                    _modoActivado.value = estado
                    _cargando.value = false
                }
        }
    }

    fun cambiarModo() {
        viewModelScope.launch {
            val nuevoEstado = !_modoActivado.value

            _guardado.value = false

            almacenamiento.guardarEstado(nuevoEstado)

            _modoActivado.value = nuevoEstado
            _guardado.value = true
        }
    }

    fun limpiarMensaje() {
        _guardado.value = false
    }
}