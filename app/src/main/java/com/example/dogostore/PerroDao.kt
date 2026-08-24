package com.example.dogostore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PerroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPerro(perro: Perro)

    @Query("SELECT * FROM perros")
    fun obtenerPerros(): Flow<List<Perro>>

    @Query("SELECT * FROM perros WHERE id = :idBuscado")
    suspend fun obtenerPerroPorId(idBuscado: Int): Perro?

    @Query("DELETE FROM perros WHERE id = :idBuscado")
    suspend fun eliminarPerroPorId(idBuscado: Int)

    @Query("DELETE FROM perros")
    suspend fun borrarTodos()
}