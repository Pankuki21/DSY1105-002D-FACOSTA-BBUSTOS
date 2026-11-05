package com.example.myapplication.viewmodel

import android.app.Activity
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.CuentaActiva
import com.example.myapplication.repository.CuentaRepository
import com.example.myapplication.repository.CuentaYaExisteException
import com.example.myapplication.repository.ProveedorOAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

data class CuentaAuthUiState(
    val correoInicio: String = "",
    val claveInicio: String = "",
    val nombreUsuario: String = "",
    val correoRegistro: String = "",
    val claveRegistro: String = "",
    val paisRegion: String = "Chile",
    val recibirNovedades: Boolean = false,
    val errorInicio: String? = null,
    val errorRegistro: String? = null,
    val errorProveedor: String? = null,
    val mensaje: String? = null,
    val registroExitoso: Boolean = false,
    val procesando: Boolean = false,
    val cuentaActiva: CuentaActiva? = null
)

class CuentaAuthViewModel(
    private val repositorio: CuentaRepository,
    private val proveedorOAuth: ProveedorOAuthRepository
) : ViewModel() {
    private val _estado = MutableStateFlow(CuentaAuthUiState())
    val estado = _estado.asStateFlow()

    fun actualizarCorreoInicio(valor: String) {
        _estado.value = _estado.value.copy(correoInicio = valor, errorInicio = null, mensaje = null)
    }

    fun actualizarClaveInicio(valor: String) {
        _estado.value = _estado.value.copy(claveInicio = valor, errorInicio = null)
    }

    fun actualizarNombreUsuario(valor: String) {
        _estado.value = _estado.value.copy(nombreUsuario = valor, errorRegistro = null)
    }

    fun actualizarCorreoRegistro(valor: String) {
        _estado.value = _estado.value.copy(correoRegistro = valor, errorRegistro = null)
    }

    fun actualizarClaveRegistro(valor: String) {
        _estado.value = _estado.value.copy(claveRegistro = valor, errorRegistro = null)
    }

    fun actualizarPaisRegion(valor: String) {
        _estado.value = _estado.value.copy(paisRegion = valor)
    }

    fun actualizarPreferenciaNovedades(valor: Boolean) {
        _estado.value = _estado.value.copy(recibirNovedades = valor)
    }

    fun iniciarSesion() {
        val actual = _estado.value
        if (!Patterns.EMAIL_ADDRESS.matcher(actual.correoInicio.trim()).matches() || actual.claveInicio.isBlank()) {
            _estado.value = actual.copy(errorInicio = "Ingresa un correo y una contraseña válidos.")
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(procesando = true, errorInicio = null, mensaje = null)
            try {
                val cuenta = repositorio.iniciarSesion(actual.correoInicio, actual.claveInicio)
                _estado.value = if (cuenta == null) {
                    _estado.value.copy(
                        procesando = false,
                        errorInicio = "El correo o la contraseña no coinciden."
                    )
                } else {
                    _estado.value.copy(
                        correoInicio = "",
                        claveInicio = "",
                        procesando = false,
                        cuentaActiva = cuenta,
                        errorInicio = null,
                        mensaje = null
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _estado.value = _estado.value.copy(
                    procesando = false,
                    errorInicio = "No se pudo iniciar sesión. Inténtalo nuevamente."
                )
            }
        }
    }

    fun registrarCuenta() {
        val actual = _estado.value
        val error = validarRegistro(actual)
        if (error != null) {
            _estado.value = actual.copy(errorRegistro = error, registroExitoso = false)
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(procesando = true, errorRegistro = null)
            try {
                repositorio.registrar(
                    nombreUsuario = actual.nombreUsuario,
                    correo = actual.correoRegistro,
                    clave = actual.claveRegistro,
                    paisRegion = actual.paisRegion,
                    recibirNovedades = actual.recibirNovedades
                )
                _estado.value = _estado.value.copy(
                    procesando = false,
                    nombreUsuario = "",
                    correoRegistro = "",
                    claveRegistro = "",
                    recibirNovedades = false,
                    registroExitoso = true,
                    mensaje = "Cuenta creada. Ya puedes iniciar sesión."
                )
            } catch (error: CancellationException) {
                throw error
            } catch (_: CuentaYaExisteException) {
                _estado.value = _estado.value.copy(
                    procesando = false,
                    errorRegistro = "Ese correo o nombre de usuario ya está registrado."
                )
            } catch (_: Exception) {
                _estado.value = _estado.value.copy(
                    procesando = false,
                    errorRegistro = "No se pudo crear la cuenta. Inténtalo nuevamente."
                )
            }
        }
    }

    fun iniciarConGoogle(activity: Activity) {
        iniciarConProveedor { proveedorOAuth.iniciarConGoogle(activity) }
    }

    fun iniciarConApple(activity: Activity) {
        iniciarConProveedor { proveedorOAuth.iniciarConApple(activity) }
    }

    private fun iniciarConProveedor(autenticar: suspend () -> CuentaActiva) {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(
                procesando = true,
                errorProveedor = null,
                errorInicio = null,
                errorRegistro = null
            )
            try {
                val cuenta = autenticar()
                _estado.value = _estado.value.copy(
                    correoInicio = "",
                    claveInicio = "",
                    procesando = false,
                    cuentaActiva = cuenta,
                    errorProveedor = null,
                    mensaje = null
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                val mensaje = error.message?.takeIf { it.startsWith("Falta configurar") || it.startsWith("Falta google-services") }
                    ?: "No se pudo iniciar con el proveedor seleccionado. Revisa su configuración e inténtalo de nuevo."
                _estado.value = _estado.value.copy(
                    procesando = false,
                    errorProveedor = mensaje
                )
            }
        }
    }

    fun consumirRegistroExitoso() {
        _estado.value = _estado.value.copy(registroExitoso = false)
    }

    fun cerrarSesion() {
        _estado.value = _estado.value.copy(
            correoInicio = "",
            claveInicio = "",
            cuentaActiva = null,
            errorInicio = null,
            mensaje = null
        )
        viewModelScope.launch {
            runCatching { proveedorOAuth.cerrarSesion() }
        }
    }

    fun limpiarMensaje() {
        _estado.value = _estado.value.copy(mensaje = null)
    }

    private fun validarRegistro(estado: CuentaAuthUiState): String? {
        val nombre = estado.nombreUsuario.trim()
        if (nombre.isEmpty() || !NOMBRE_USUARIO.matches(nombre)) {
            return "El nombre de usuario admite letras, números y guiones internos."
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(estado.correoRegistro.trim()).matches()) {
            return "Ingresa un correo electrónico válido."
        }
        val claveValida = estado.claveRegistro.length >= 15 ||
            (estado.claveRegistro.length >= 8 &&
                estado.claveRegistro.any(Char::isDigit) &&
                estado.claveRegistro.any(Char::isLowerCase))
        if (!claveValida) {
            return "Usa al menos 15 caracteres o 8 con un número y una minúscula."
        }
        if (estado.paisRegion.isBlank()) return "Selecciona tu país o región."
        return null
    }

    private companion object {
        val NOMBRE_USUARIO = Regex("^[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*$")
    }
}
