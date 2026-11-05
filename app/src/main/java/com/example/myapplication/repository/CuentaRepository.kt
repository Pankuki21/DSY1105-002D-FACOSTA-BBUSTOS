package com.example.myapplication.repository

import com.example.myapplication.data.CuentaDao
import com.example.myapplication.data.CuentaEntity
import com.example.myapplication.utils.HashClave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CuentaActiva(val nombreUsuario: String, val correo: String)

class CuentaYaExisteException : IllegalArgumentException("El correo o nombre de usuario ya está registrado.")

class CuentaRepository(private val cuentaDao: CuentaDao) {
    suspend fun registrar(
        nombreUsuario: String,
        correo: String,
        clave: String,
        paisRegion: String,
        recibirNovedades: Boolean
    ) {
        val usuarioNormalizado = nombreUsuario.trim().lowercase()
        val correoNormalizado = correo.trim().lowercase()

        if (cuentaDao.obtenerPorCorreo(correoNormalizado) != null ||
            cuentaDao.obtenerPorNombreUsuario(usuarioNormalizado) != null
        ) {
            throw CuentaYaExisteException()
        }

        val claveCifrada = withContext(Dispatchers.Default) {
            HashClave.crear(clave)
        }
        cuentaDao.crear(
            CuentaEntity(
                nombreUsuario = usuarioNormalizado,
                correo = correoNormalizado,
                claveHash = claveCifrada.hash,
                claveSalt = claveCifrada.salt,
                paisRegion = paisRegion,
                recibirNovedades = recibirNovedades,
                fechaRegistro = System.currentTimeMillis()
            )
        )
    }

    suspend fun iniciarSesion(correo: String, clave: String): CuentaActiva? {
        val cuenta = cuentaDao.obtenerPorCorreo(correo.trim().lowercase()) ?: return null
        val claveValida = withContext(Dispatchers.Default) {
            HashClave.verificar(clave, cuenta.claveHash, cuenta.claveSalt)
        }
        if (!claveValida) return null
        return CuentaActiva(cuenta.nombreUsuario, cuenta.correo)
    }
}
