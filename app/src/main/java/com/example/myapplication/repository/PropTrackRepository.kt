package com.example.myapplication.repository

import com.example.myapplication.data.ClienteEntity
import com.example.myapplication.data.DepartamentoEntity
import com.example.myapplication.data.EdificioEntity
import com.example.myapplication.data.PropTrackDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PropTrackRepository(private val database: PropTrackDatabase) {
    val edificios: Flow<List<EdificioEntity>> = database.edificioDao().observarTodos()
    val departamentos: Flow<List<DepartamentoEntity>> = database.departamentoDao().observarTodos()
    val propiedadesDisponibles: Flow<List<DepartamentoEntity>> = database.departamentoDao().observarDisponibles()
    val clientes: Flow<List<ClienteEntity>> = database.clienteDao().observarTodos()

    suspend fun registrarDepartamento(
        edificioNombre: String,
        direccion: String,
        comuna: String,
        cantidadDepartamentos: Int,
        numero: String,
        piso: Int,
        metrosCuadrados: Double,
        dormitorios: Int,
        banos: Int,
        terraza: Boolean,
        precio: Double,
        tipoOperacion: String,
        estado: String
    ) {
        val nombre = edificioNombre.trim()
        val direccionLimpia = direccion.trim()
        val comunaLimpia = comuna.trim()
        val edificioDao = database.edificioDao()
        edificioDao.insertar(
            EdificioEntity(
                nombre = nombre,
                direccion = direccionLimpia,
                comuna = comunaLimpia,
                cantidadDepartamentos = cantidadDepartamentos
            )
        )
        val edificioId = edificioDao.buscarId(nombre, direccionLimpia, comunaLimpia)
            ?: error("No se pudo guardar el edificio")
        database.departamentoDao().insertar(
            DepartamentoEntity(
                edificioId = edificioId,
                numero = numero.trim(),
                piso = piso,
                metrosCuadrados = metrosCuadrados,
                dormitorios = dormitorios,
                banos = banos,
                terraza = terraza,
                precio = precio,
                tipoOperacion = tipoOperacion,
                estado = estado
            )
        )
    }

    suspend fun registrarCliente(cliente: ClienteEntity) {
        database.clienteDao().insertar(cliente)
    }

    fun clientesCompatibles(propiedad: DepartamentoEntity): Flow<List<ClienteEntity>> =
        clientes.map { lista ->
            lista.filter { cliente ->
                if (cliente.tipoOperacion != propiedad.tipoOperacion) return@filter false
                val capacidad = if (propiedad.tipoOperacion == "ARRIENDO") {
                    cliente.rentaMensual > 0 && cliente.rentaMensual >= propiedad.precio * 2.5
                } else {
                    cliente.presupuesto > 0 && cliente.presupuesto >= propiedad.precio
                }
                val preferencias = cliente.preferencias.lowercase()
                val coincideDormitorios = Regex("(\\d+)\\s*dormitorio").find(preferencias)
                    ?.groupValues?.get(1)?.toIntOrNull()?.let { propiedad.dormitorios >= it } ?: true
                val requiereTerraza = "terraza" in preferencias
                capacidad && coincideDormitorios && (!requiereTerraza || propiedad.terraza)
            }
        }
}
