package com.example.myapplication.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "edificios", indices = [Index(value = ["nombre", "direccion", "comuna"], unique = true)])
data class EdificioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val direccion: String,
    val comuna: String,
    val cantidadDepartamentos: Int
)

@Entity(
    tableName = "departamentos",
    foreignKeys = [ForeignKey(entity = EdificioEntity::class, parentColumns = ["id"], childColumns = ["edificioId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("edificioId"), Index("estado"), Index("tipoOperacion")]
)
data class DepartamentoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val edificioId: Long,
    val numero: String,
    val piso: Int,
    val metrosCuadrados: Double,
    val dormitorios: Int,
    val banos: Int,
    val terraza: Boolean,
    val precio: Double,
    val tipoOperacion: String,
    val estado: String
)

@Entity(tableName = "clientes", indices = [Index(value = ["correo"], unique = true)])
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val tipoOperacion: String,
    val rentaMensual: Double,
    val presupuesto: Double,
    val preferencias: String,
    val fotoUri: String? = null
)

@Dao
interface EdificioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(edificio: EdificioEntity): Long

    @Query("SELECT id FROM edificios WHERE nombre = :nombre AND direccion = :direccion AND comuna = :comuna LIMIT 1")
    suspend fun buscarId(nombre: String, direccion: String, comuna: String): Long?

    @Query("SELECT * FROM edificios ORDER BY nombre")
    fun observarTodos(): Flow<List<EdificioEntity>>
}

@Dao
interface DepartamentoDao {
    @Insert
    suspend fun insertar(departamento: DepartamentoEntity): Long

    @Query("SELECT * FROM departamentos ORDER BY id DESC")
    fun observarTodos(): Flow<List<DepartamentoEntity>>

    @Query("SELECT * FROM departamentos WHERE estado = 'DISPONIBLE' ORDER BY id DESC")
    fun observarDisponibles(): Flow<List<DepartamentoEntity>>
}

@Dao
interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(cliente: ClienteEntity): Long

    @Query("SELECT * FROM clientes ORDER BY nombre COLLATE NOCASE")
    fun observarTodos(): Flow<List<ClienteEntity>>
}

@Database(entities = [EdificioEntity::class, DepartamentoEntity::class, ClienteEntity::class], version = 1, exportSchema = false)
abstract class PropTrackDatabase : RoomDatabase() {
    abstract fun edificioDao(): EdificioDao
    abstract fun departamentoDao(): DepartamentoDao
    abstract fun clienteDao(): ClienteDao

    companion object {
        @Volatile private var INSTANCE: PropTrackDatabase? = null

        fun getDatabase(context: Context): PropTrackDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, PropTrackDatabase::class.java, "proptrack.db")
                .build()
                .also { INSTANCE = it }
        }
    }
}
