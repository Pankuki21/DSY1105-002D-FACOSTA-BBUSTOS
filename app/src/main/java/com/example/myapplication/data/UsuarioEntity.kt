package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "usuarios")
@TypeConverters(Converters::class)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val correo: String,

    val direccion: String,

    val modoEspecial: Boolean,

    val fotoPath: String?,

    val fechaRegistro: Date
)