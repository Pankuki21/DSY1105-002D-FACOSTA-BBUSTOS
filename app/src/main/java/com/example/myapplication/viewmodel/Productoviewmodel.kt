package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Producto
import com.example.myapplication.repository.ProductoRepository

class ProductoViewModel : ViewModel() {
    private val repository = ProductoRepository()
    val listaProductos: List<Producto> = repository.obtenerProductos()
}