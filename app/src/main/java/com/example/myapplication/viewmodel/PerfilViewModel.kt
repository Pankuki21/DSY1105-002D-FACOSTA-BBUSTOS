package com.example.myapplication.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Application.perfilDataStore by preferencesDataStore(
    name = "preferencias_perfil"
)

class PerfilViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore = application.perfilDataStore

    private val _imagenPerfil = MutableStateFlow<Uri?>(null)

    val imagenPerfil: StateFlow<Uri?> =
        _imagenPerfil.asStateFlow()

    private companion object {
        val IMAGEN_PERFIL = stringPreferencesKey("imagen_perfil")
    }

    init {
        viewModelScope.launch {
            try {
                val preferencias = dataStore.data.first()
                val uriGuardada = preferencias[IMAGEN_PERFIL]

                if (uriGuardada != null) {
                    _imagenPerfil.value = Uri.parse(uriGuardada)
                }
            } catch (e: Exception) {
                _imagenPerfil.value = null
            }
        }
    }

    fun actualizarImagen(uri: Uri?) {
        viewModelScope.launch {
            try {
                dataStore.edit { preferencias ->
                    if (uri != null) {
                        preferencias[IMAGEN_PERFIL] = uri.toString()
                    } else {
                        preferencias.remove(IMAGEN_PERFIL)
                    }
                }

                _imagenPerfil.value = uri
            } catch (e: Exception) {
                // Si no se puede guardar, se conserva el estado actual.
            }
        }
    }
}