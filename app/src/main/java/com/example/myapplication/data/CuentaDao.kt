package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CuentaDao {
    @Query("SELECT * FROM cuentas WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): CuentaEntity?

    @Query("SELECT * FROM cuentas WHERE nombreUsuario = :nombreUsuario LIMIT 1")
    suspend fun obtenerPorNombreUsuario(nombreUsuario: String): CuentaEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun crear(cuenta: CuentaEntity): Long
}
