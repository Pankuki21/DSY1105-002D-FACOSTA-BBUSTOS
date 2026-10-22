package com.example.myapplication.repository

import com.example.myapplication.data.UsuarioDao
import com.example.myapplication.data.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    fun obtenerUsuarios(): Flow<List<UsuarioEntity>> {
        return usuarioDao.getUsuariosFlow()
    }

    suspend fun obtenerUsuarioPorId(id: Int): UsuarioEntity? {
        return usuarioDao.getPorId(id)
    }

    suspend fun insertarUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertar(usuario)
    }

    suspend fun actualizarUsuario(usuario: UsuarioEntity) {
        usuarioDao.actualizar(usuario)
    }

    suspend fun eliminarUsuario(usuario: UsuarioEntity) {
        usuarioDao.eliminar(usuario)
    }

    fun buscarUsuariosPorCorreo(query: String): Flow<List<UsuarioEntity>> {
        return usuarioDao.buscarPorCorreo(query)
    }
}