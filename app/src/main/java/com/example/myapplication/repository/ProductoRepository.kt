package com.example.myapplication.repository

import com.example.myapplication.model.Producto

class ProductoRepository {
    fun obtenerProductos(): List<Producto> {
        return listOf(
            Producto(1, "Elemento Base 1", 10.0),
            Producto(2, "Elemento Base 2", 20.0)
        )
    }
}