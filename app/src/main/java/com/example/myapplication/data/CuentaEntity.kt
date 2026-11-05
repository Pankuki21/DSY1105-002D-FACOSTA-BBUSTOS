package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cuentas",
    indices = [
        Index(value = ["correo"], unique = true),
        Index(value = ["nombreUsuario"], unique = true)
    ]
)
data class CuentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombreUsuario: String,
    val correo: String,
    val claveHash: String,
    val claveSalt: String,
    val paisRegion: String,
    val recibirNovedades: Boolean,
    val fechaRegistro: Long
)
