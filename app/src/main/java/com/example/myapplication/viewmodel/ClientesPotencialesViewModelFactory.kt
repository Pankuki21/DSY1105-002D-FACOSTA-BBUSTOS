package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.ClientesPotencialesRepository

class ClientesPotencialesViewModelFactory(
    private val repositorio: ClientesPotencialesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientesPotencialesViewModel::class.java)) {
            return ClientesPotencialesViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Tipo de ViewModel no compatible")
    }
}
