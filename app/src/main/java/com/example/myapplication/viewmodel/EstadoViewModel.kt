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

    // Estado del modo especial que observa la interfaz.
    private val _modoActivado = MutableStateFlow(false)
    val modoActivado: StateFlow<Boolean> =
        _modoActivado.asStateFlow()

    // Indica si se está recuperando el estado guardado.
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Indica que el estado se guardó correctamente.
    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado.asStateFlow()

    init {
        // Recupera el último estado guardado al iniciar.
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

    // Cambia el modo y persiste el nuevo estado.
    fun cambiarModo() {
        viewModelScope.launch {
            val nuevoEstado = !_modoActivado.value
            _guardado.value = false

            almacenamiento.guardarEstado(nuevoEstado)
            _modoActivado.value = nuevoEstado
            _guardado.value = true
        }
    }

    // Oculta el mensaje de confirmación.
    fun limpiarMensaje() {
        _guardado.value = false
    }
}
