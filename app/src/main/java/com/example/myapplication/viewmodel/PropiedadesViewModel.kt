package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.DepartamentoEntity
import com.example.myapplication.data.PropTrackApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PropiedadFormState(
    val edificio: String = "", val direccion: String = "", val comuna: String = "",
    val cantidadDepartamentos: String = "", val numero: String = "", val piso: String = "",
    val metrosCuadrados: String = "", val dormitorios: String = "", val banos: String = "",
    val terraza: Boolean = false, val precio: String = "", val tipoOperacion: String = "ARRIENDO",
    val estado: String = "DISPONIBLE", val error: String? = null, val guardado: Boolean = false,
    val guardando: Boolean = false
)

class PropiedadesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PropTrackApplication).repository
    private val _form = MutableStateFlow(PropiedadFormState())
    val form = _form.asStateFlow()
    val disponibles = repository.propiedadesDisponibles
    val propiedades = repository.departamentos
    val edificios = repository.edificios

    fun actualizar(campo: String, valor: String) {
        _form.value = when (campo) {
            "edificio" -> _form.value.copy(edificio = valor)
            "direccion" -> _form.value.copy(direccion = valor)
            "comuna" -> _form.value.copy(comuna = valor)
            "cantidadDepartamentos" -> _form.value.copy(cantidadDepartamentos = valor)
            "numero" -> _form.value.copy(numero = valor)
            "piso" -> _form.value.copy(piso = valor)
            "metrosCuadrados" -> _form.value.copy(metrosCuadrados = valor)
            "dormitorios" -> _form.value.copy(dormitorios = valor)
            "banos" -> _form.value.copy(banos = valor)
            "precio" -> _form.value.copy(precio = valor)
            "tipoOperacion" -> _form.value.copy(tipoOperacion = valor.uppercase())
            "estado" -> _form.value.copy(estado = valor.uppercase())
            else -> _form.value
        }.copy(error = null, guardado = false)
    }

    fun cambiarTerraza(valor: Boolean) { _form.value = _form.value.copy(terraza = valor, error = null, guardado = false) }

    fun guardar(onGuardado: () -> Unit = {}) {
        val f = _form.value
        val cantidad = f.cantidadDepartamentos.toIntOrNull()
        val piso = f.piso.toIntOrNull()
        val m2 = f.metrosCuadrados.toDoubleOrNull()
        val dormitorios = f.dormitorios.toIntOrNull()
        val banos = f.banos.toIntOrNull()
        val precio = f.precio.toDoubleOrNull()
        val operacion = f.tipoOperacion.uppercase()
        val estado = f.estado.uppercase()
        val error = when {
            listOf(f.edificio, f.direccion, f.comuna, f.numero).any(String::isBlank) -> "Completa los datos del edificio y el número del departamento."
            cantidad == null || cantidad < 1 -> "La cantidad de departamentos del edificio debe ser mayor que cero."
            piso == null || piso < 0 || m2 == null || m2 <= 0 || dormitorios == null || dormitorios < 0 || banos == null || banos < 0 || precio == null || precio <= 0 -> "Revisa los campos numéricos: deben contener valores válidos."
            operacion !in setOf("ARRIENDO", "COMPRA") -> "El tipo de operación debe ser Arriendo o Compra."
            estado !in setOf("DISPONIBLE", "RESERVADO", "VENDIDO", "ARRENDADO") -> "El estado debe ser Disponible, Reservado, Vendido o Arrendado."
            else -> null
        }
        if (error != null) { _form.value = f.copy(error = error, guardado = false); return }
        _form.value = f.copy(error = null, guardando = true)
        viewModelScope.launch {
            runCatching {
                repository.registrarDepartamento(f.edificio, f.direccion, f.comuna, cantidad!!, f.numero,
                    piso!!, m2!!, dormitorios!!, banos!!, f.terraza, precio!!, operacion, estado)
            }.onSuccess {
                _form.value = PropiedadFormState(guardado = true)
                onGuardado()
            }.onFailure { e ->
                _form.value = _form.value.copy(guardando = false, error = e.localizedMessage ?: "No se pudo guardar la propiedad.")
            }
        }
    }

    fun coincidencias(propiedad: DepartamentoEntity) = repository.clientesCompatibles(propiedad)
}
