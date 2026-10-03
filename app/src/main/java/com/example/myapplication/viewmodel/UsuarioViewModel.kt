package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.UsuarioErrores
import com.example.myapplication.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUiState())

    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(
                direccion = valor,
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(
                aceptaTerminos = valor
            )
        }
    }

    fun validarFormulario(): Boolean {

        val estadoActual = _estado.value

        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) {
                "El nombre es obligatorio"
            } else {
                null
            },

            correo = if (
                estadoActual.correo.isBlank()
            ) {
                "El correo es obligatorio"
            } else if (
                !estadoActual.correo.contains("@")
            ) {
                "Ingresa un correo válido"
            } else {
                null
            },

            clave = if (estadoActual.clave.isBlank()) {
                "La clave es obligatoria"
            } else if (estadoActual.clave.length < 6) {
                "La clave debe tener al menos 6 caracteres"
            } else {
                null
            },

            direccion = if (estadoActual.direccion.isBlank()) {
                "La dirección es obligatoria"
            } else {
                null
            }
        )

        _estado.update {
            it.copy(
                errores = errores
            )
        }

        return errores.nombre == null &&
                errores.correo == null &&
                errores.clave == null &&
                errores.direccion == null
    }
}