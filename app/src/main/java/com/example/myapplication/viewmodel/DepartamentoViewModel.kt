package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Departamento
import com.example.myapplication.model.EstadoDepartamento
import com.example.myapplication.model.TipoOperacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DepartamentoUiState(
    val numero: String = "",
    val piso: String = "",
    val metrosCuadrados: String = "",
    val dormitorios: String = "",
    val banos: String = "",
    val terraza: Boolean = false,
    val precio: String = "",
    val tipoOperacion: String = "",
    val estado: String = "",
    val error: String? = null,
    val guardado: Boolean = false
)

class DepartamentoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DepartamentoUiState())
    val uiState: StateFlow<DepartamentoUiState> = _uiState.asStateFlow()

    fun cambiarNumero(valor: String) {
        _uiState.value = _uiState.value.copy(
            numero = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarPiso(valor: String) {
        _uiState.value = _uiState.value.copy(
            piso = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarMetrosCuadrados(valor: String) {
        _uiState.value = _uiState.value.copy(
            metrosCuadrados = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarDormitorios(valor: String) {
        _uiState.value = _uiState.value.copy(
            dormitorios = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarBanos(valor: String) {
        _uiState.value = _uiState.value.copy(
            banos = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarTerraza(valor: Boolean) {
        _uiState.value = _uiState.value.copy(
            terraza = valor,
            guardado = false
        )
    }

    fun cambiarPrecio(valor: String) {
        _uiState.value = _uiState.value.copy(
            precio = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarTipoOperacion(valor: String) {
        _uiState.value = _uiState.value.copy(
            tipoOperacion = valor,
            error = null,
            guardado = false
        )
    }

    fun cambiarEstado(valor: String) {
        _uiState.value = _uiState.value.copy(
            estado = valor,
            error = null,
            guardado = false
        )
    }

    fun validarFormulario(): Boolean {

        val estado = _uiState.value

        if (
            estado.numero.isBlank() ||
            estado.piso.isBlank() ||
            estado.metrosCuadrados.isBlank() ||
            estado.dormitorios.isBlank() ||
            estado.banos.isBlank() ||
            estado.precio.isBlank() ||
            estado.tipoOperacion.isBlank() ||
            estado.estado.isBlank()
        ) {
            _uiState.value = estado.copy(
                error = "Completa todos los campos",
                guardado = false
            )

            return false
        }

        if (
            estado.piso.toIntOrNull() == null ||
            estado.metrosCuadrados.toDoubleOrNull() == null ||
            estado.dormitorios.toIntOrNull() == null ||
            estado.banos.toIntOrNull() == null ||
            estado.precio.toDoubleOrNull() == null
        ) {
            _uiState.value = estado.copy(
                error = "Los campos numéricos deben contener valores válidos",
                guardado = false
            )

            return false
        }

        _uiState.value = estado.copy(
            error = null,
            guardado = true
        )

        return true
    }

    fun obtenerDepartamento(): Departamento? {

        val estado = _uiState.value

        if (!validarFormulario()) {
            return null
        }

        return Departamento(
            id = 1,
            numero = estado.numero,
            piso = estado.piso.toInt(),
            metrosCuadrados = estado.metrosCuadrados.toDouble(),
            dormitorios = estado.dormitorios.toInt(),
            banos = estado.banos.toInt(),
            terraza = estado.terraza,
            precio = estado.precio.toDouble(),
            tipoOperacion = when (estado.tipoOperacion.uppercase()) {
                "COMPRA" -> TipoOperacion.COMPRA
                else -> TipoOperacion.ARRIENDO
            },
            estado = when (estado.estado.uppercase()) {
                "RESERVADO" -> EstadoDepartamento.RESERVADO
                "VENDIDO" -> EstadoDepartamento.VENDIDO
                "ARRENDADO" -> EstadoDepartamento.ARRENDADO
                else -> EstadoDepartamento.DISPONIBLE
            }
        )
    }
}