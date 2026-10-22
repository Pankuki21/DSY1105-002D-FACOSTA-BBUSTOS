package com.example.myapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.EstadoDataStore
import com.example.myapplication.data.UsuarioEntity
import com.example.myapplication.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.Date
import android.content.Context
import com.example.myapplication.utils.guardarFotoLocal

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val modoEspecial: Boolean = false,
    val aceptaTerminos: Boolean = false,
    val fotoUri: Uri? = null,
    val errores: UsuarioErrores = UsuarioErrores(),
    val cargando: Boolean = true,
    val guardando: Boolean = false,
    val registroExitoso: Boolean = false,
    val mensaje: String? = null
)

data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val terminos: String? = null,
    val foto: String? = null
)

class UsuarioViewModel(
    private val repository: UsuarioRepository,
    private val estadoDataStore: EstadoDataStore,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            estadoDataStore.obtenerEstado().collect { activado ->
                _uiState.update {
                    it.copy(modoEspecial = activado)
                }
            }
        }

        viewModelScope.launch {
            android.util.Log.d(
                "PropTrackCarga",
                "INICIO: cargando = ${_uiState.value.cargando}"
            )

            delay(1500)

            _uiState.update {
                it.copy(cargando = false)
            }

            android.util.Log.d(
                "PropTrackCarga",
                "FIN: cargando = ${_uiState.value.cargando}"
            )
        }
    }

    fun actualizarNombre(valor: String) {
        _uiState.value = _uiState.value.copy(
            nombre = valor,
            errores = _uiState.value.errores.copy(nombre = null)
        )
    }

    fun actualizarCorreo(valor: String) {
        _uiState.value = _uiState.value.copy(
            correo = valor,
            errores = _uiState.value.errores.copy(correo = null)
        )
    }

    fun actualizarClave(valor: String) {
        _uiState.value = _uiState.value.copy(
            clave = valor,
            errores = _uiState.value.errores.copy(clave = null)
        )
    }

    fun actualizarDireccion(valor: String) {
        _uiState.value = _uiState.value.copy(
            direccion = valor,
            errores = _uiState.value.errores.copy(direccion = null)
        )
    }

    fun actualizarModoEspecial(valor: Boolean) {
        viewModelScope.launch {
            try{
                estadoDataStore.guardarEstado(valor)
                _uiState.value = _uiState.value.copy(
                    modoEspecial = valor,
                    mensaje = if (valor){
                        "Modo especial activado"
                    }else{
                        "Modo especial desactivado"
                    }
                )
            }catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    mensaje = "No se pudo guardar la preferencia"
                )
            }
        }
    }

    fun actualizarAceptacionTerminos(valor: Boolean) {
        _uiState.value = _uiState.value.copy(
            aceptaTerminos = valor,
            errores = _uiState.value.errores.copy(terminos = null)
        )
    }

    fun actualizarFoto(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            fotoUri = uri,
            errores = _uiState.value.errores.copy(foto = null)
        )
    }

    fun consumirRegistroExitoso() {
        _uiState.value = _uiState.value.copy(
            registroExitoso = false
        )
    }

    private fun validarFormulario(): UsuarioErrores {
        val estado = _uiState.value

        return UsuarioErrores(
            nombre = if (estado.nombre.trim().length < 3) {
                "El nombre debe tener al menos 3 caracteres."
            } else null,

            correo = if (
                estado.correo.isBlank() ||
                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(estado.correo.trim())
                    .matches()
            ) {
                "Ingresa un correo electrónico válido."
            } else null,

            clave = if (estado.clave.length < 6) {
                "La clave debe tener al menos 6 caracteres."
            } else null,

            direccion = if (estado.direccion.isBlank()) {
                "La dirección es obligatoria."
            } else null,

            terminos = if (!estado.aceptaTerminos) {
                "Debes aceptar los términos y condiciones."
            } else null,

            foto = if (estado.fotoUri == null) {
                "Debes seleccionar una fotografía."
            } else null
        )
    }

    fun registrarUsuario() {
        val errores = validarFormulario()

        if (
            errores.nombre != null ||
            errores.correo != null ||
            errores.clave != null ||
            errores.direccion != null ||
            errores.terminos != null ||
            errores.foto != null
        ) {
            _uiState.value = _uiState.value.copy(
                errores = errores,
                registroExitoso = false,
                mensaje = null
            )
            return
        }

        val estado = _uiState.value

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                guardando = true,
                registroExitoso = false,
                mensaje = null
            )

            try {
                val rutaFoto = guardarFotoLocal(
                    context = context,
                    uri = estado.fotoUri!!
                )

                val usuario = UsuarioEntity(
                    nombre = estado.nombre.trim(),
                    correo = estado.correo.trim(),
                    direccion = estado.direccion.trim(),
                    modoEspecial = estado.modoEspecial,
                    fotoPath = rutaFoto,
                    fechaRegistro = Date()
                )

                repository.insertarUsuario(usuario)

                _uiState.value = _uiState.value.copy(
                    guardando = false,
                    registroExitoso = true,
                    mensaje = "Usuario registrado correctamente.",
                    errores = UsuarioErrores()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    guardando = false,
                    registroExitoso = false,
                    mensaje = "No se pudo registrar el usuario. Inténtalo nuevamente."
                )
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = UsuarioUiState()
    }

    fun limpiarMensaje() {
        _uiState.value = _uiState.value.copy(mensaje = null)
    }
}