package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ClienteEntity
import com.example.myapplication.data.PropTrackApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClienteFormState(
    val nombre: String = "", val telefono: String = "", val correo: String = "",
    val tipoOperacion: String = "ARRIENDO", val rentaMensual: String = "", val presupuesto: String = "",
    val preferencias: String = "", val error: String? = null, val guardado: Boolean = false, val guardando: Boolean = false
)

class ClientesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PropTrackApplication).repository
    private val _form = MutableStateFlow(ClienteFormState())
    val form = _form.asStateFlow()
    val clientes = repository.clientes

    fun actualizar(campo: String, valor: String) {
        _form.value = when (campo) {
            "nombre" -> _form.value.copy(nombre = valor)
            "telefono" -> _form.value.copy(telefono = valor)
            "correo" -> _form.value.copy(correo = valor)
            "tipoOperacion" -> _form.value.copy(tipoOperacion = valor.uppercase())
            "rentaMensual" -> _form.value.copy(rentaMensual = valor)
            "presupuesto" -> _form.value.copy(presupuesto = valor)
            "preferencias" -> _form.value.copy(preferencias = valor)
            else -> _form.value
        }.copy(error = null, guardado = false)
    }

    fun guardar(onGuardado: () -> Unit = {}) {
        val f = _form.value
        val operacion = f.tipoOperacion.uppercase()
        val renta = f.rentaMensual.toDoubleOrNull() ?: 0.0
        val presupuesto = f.presupuesto.toDoubleOrNull() ?: 0.0
        val error = when {
            f.nombre.trim().length < 2 -> "Ingresa el nombre del cliente."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(f.correo.trim()).matches() -> "Ingresa un correo válido."
            f.telefono.filter(Char::isDigit).length < 8 -> "Ingresa un teléfono válido."
            operacion !in setOf("ARRIENDO", "COMPRA") -> "El tipo de operación debe ser Arriendo o Compra."
            (operacion == "ARRIENDO" && (renta <= 0 || f.rentaMensual.toDoubleOrNull() == null)) -> "Indica la renta mensual del cliente."
            (operacion == "COMPRA" && (presupuesto <= 0 || f.presupuesto.toDoubleOrNull() == null)) -> "Indica el presupuesto de compra."
            else -> null
        }
        if (error != null) { _form.value = f.copy(error = error, guardado = false); return }
        _form.value = f.copy(error = null, guardando = true)
        viewModelScope.launch {
            runCatching {
                repository.registrarCliente(ClienteEntity(nombre = f.nombre.trim(), telefono = f.telefono.trim(), correo = f.correo.trim(),
                    tipoOperacion = operacion, rentaMensual = renta, presupuesto = presupuesto, preferencias = f.preferencias.trim()))
            }.onSuccess {
                _form.value = ClienteFormState(guardado = true)
                onGuardado()
            }.onFailure { e ->
                val mensaje = if (e.message.orEmpty().contains("UNIQUE", true)) "Ya existe un cliente registrado con ese correo." else e.localizedMessage ?: "No se pudo guardar el cliente."
                _form.value = _form.value.copy(guardando = false, error = mensaje)
            }
        }
    }
}
