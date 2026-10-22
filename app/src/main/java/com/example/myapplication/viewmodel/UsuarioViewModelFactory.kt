package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.EstadoDataStore
import com.example.myapplication.repository.UsuarioRepository

class UsuarioViewModelFactory(
    private val repository: UsuarioRepository,
    private val estadoDataStore: EstadoDataStore,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(
                repository = repository,
                estadoDataStore = estadoDataStore,
                context = context.applicationContext
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}