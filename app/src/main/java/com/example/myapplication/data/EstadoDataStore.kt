package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "preferencias_usuario"
)

class EstadoDataStore(private val context: Context) {

    private companion object {
        val MODO_ACTIVADO = booleanPreferencesKey("modo_activado")
    }

    // Guarda el estado del modo especial.
    suspend fun guardarEstado(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_ACTIVADO] = activado
        }
    }

    // Recupera el estado guardado como un flujo reactivo.
    fun obtenerEstado(): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[MODO_ACTIVADO] ?: false
        }
    }
}
