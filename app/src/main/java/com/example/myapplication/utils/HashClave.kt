package com.example.myapplication.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

data class ClaveCifrada(val hash: String, val salt: String)

object HashClave {
    private const val ITERACIONES = 120_000
    private const val BITS = 256

    fun crear(clave: String): ClaveCifrada {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        return ClaveCifrada(
            hash = derivar(clave, salt),
            salt = Base64.encodeToString(salt, Base64.NO_WRAP)
        )
    }

    fun verificar(clave: String, hashGuardado: String, saltGuardada: String): Boolean {
        val salt = Base64.decode(saltGuardada, Base64.NO_WRAP)
        val hashCalculado = derivar(clave, salt)
        return MessageDigest.isEqual(
            Base64.decode(hashGuardado, Base64.NO_WRAP),
            Base64.decode(hashCalculado, Base64.NO_WRAP)
        )
    }

    private fun derivar(clave: String, salt: ByteArray): String {
        val spec = PBEKeySpec(clave.toCharArray(), salt, ITERACIONES, BITS)
        return try {
            val bytes = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .encoded
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } finally {
            spec.clearPassword()
        }
    }
}
