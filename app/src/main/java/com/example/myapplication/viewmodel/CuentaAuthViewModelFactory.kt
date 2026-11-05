package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.CuentaRepository
import com.example.myapplication.repository.ProveedorOAuthRepository

class CuentaAuthViewModelFactory(
    private val repositorio: CuentaRepository,
    private val proveedorOAuth: ProveedorOAuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CuentaAuthViewModel::class.java)) {
            return CuentaAuthViewModel(repositorio, proveedorOAuth) as T
        }
        throw IllegalArgumentException("Tipo de ViewModel no compatible")
    }
}
