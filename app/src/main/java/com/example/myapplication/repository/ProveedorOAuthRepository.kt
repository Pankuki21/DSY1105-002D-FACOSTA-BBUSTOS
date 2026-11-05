package com.example.myapplication.repository

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.CredentialOption
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class ProveedorOAuthRepository(context: Context) {
    private val appContext = context.applicationContext
    private val administradorCredenciales = CredentialManager.create(appContext)

    suspend fun iniciarConGoogle(activity: Activity): CuentaActiva {
        val auth = obtenerFirebaseAuth()
        val recursoClientId = appContext.resources.getIdentifier(
            "default_web_client_id",
            "string",
            appContext.packageName
        )
        if (recursoClientId == 0) {
            throw IllegalStateException("Falta google-services.json en el módulo app.")
        }

        val clientId = appContext.getString(recursoClientId)
        val opcionGoogle: CredentialOption = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            GetSignInWithGoogleOption.Builder(clientId).build()
        } else {
            GetGoogleIdOption.Builder()
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .build()
        }
        val solicitud = GetCredentialRequest.Builder()
            .addCredentialOption(opcionGoogle)
            .build()
        val respuesta = administradorCredenciales.getCredential(activity, solicitud)
        val credencial = respuesta.credential as? CustomCredential
            ?: throw IllegalStateException("Google no devolvió una credencial compatible.")
        if (credencial.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            throw IllegalStateException("Google no devolvió un token de acceso válido.")
        }

        val token = GoogleIdTokenCredential.createFrom(credencial.data).idToken
        val resultado = auth.signInWithCredential(
            GoogleAuthProvider.getCredential(token, null)
        ).await()
        val usuario = resultado.user ?: throw IllegalStateException("No se recibió el perfil de Google.")
        return CuentaActiva(
            nombreUsuario = usuario.displayName?.takeIf(String::isNotBlank)
                ?: usuario.email?.substringBefore('@').orEmpty(),
            correo = usuario.email.orEmpty()
        )
    }

    suspend fun iniciarConApple(activity: Activity): CuentaActiva {
        val auth = obtenerFirebaseAuth()
        val proveedor = OAuthProvider.newBuilder("apple.com")
            .setScopes(listOf("email", "name"))
            .addCustomParameter("locale", "es")
            .build()
        val resultadoPendiente = auth.pendingAuthResult
        val resultado = if (resultadoPendiente != null) {
            resultadoPendiente.await()
        } else {
            auth.startActivityForSignInWithProvider(activity, proveedor).await()
        }
        val usuario = resultado.user ?: throw IllegalStateException("No se recibió el perfil de Apple.")
        return CuentaActiva(
            nombreUsuario = usuario.displayName?.takeIf(String::isNotBlank)
                ?: usuario.email?.substringBefore('@').orEmpty(),
            correo = usuario.email.orEmpty()
        )
    }

    suspend fun cerrarSesion() {
        if (FirebaseApp.getApps(appContext).isNotEmpty()) {
            FirebaseAuth.getInstance().signOut()
        }
        runCatching {
            administradorCredenciales.clearCredentialState(ClearCredentialStateRequest())
        }
    }

    private fun obtenerFirebaseAuth(): FirebaseAuth {
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            throw IllegalStateException(
                "Falta configurar Firebase. Agrega google-services.json y habilita los proveedores OAuth."
            )
        }
        return FirebaseAuth.getInstance()
    }
}
