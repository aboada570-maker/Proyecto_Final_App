package com.example.dogostore

import kotlinx.coroutines.flow.Flow

// El repositorio pide el DAO (Room) y el API (Retrofit) en su constructor
class PerroRepository(
    private val perroDao: PerroDao,
    private val dogApi: DogApi
) {

    // --- 1. OPERACIONES LOCALES (ROOM) ---

    // Obtenemos el Flow de la base de datos
    val perrosLocales: Flow<List<Perro>> = perroDao.obtenerPerros()

    suspend fun insertarPerroLocal(perro: Perro) {
        perroDao.insertarPerro(perro)
    }

    suspend fun obtenerPerroLocalPorId(id: Int): Perro? {
        return perroDao.obtenerPerroPorId(id)
    }

    suspend fun eliminarPerroLocal(id: Int) {
        perroDao.eliminarPerroPorId(id)
    }

    // --- 2. OPERACIONES DE RED (RETROFIT) ---

    suspend fun obtenerRazasDelMundo(): List<RazaRed> {
        return try {
            val respuesta = dogApi.obtenerRazas()
            respuesta.resultados
        } catch (e: Exception) {
            android.util.Log.e("DogoStore", "Error en Repositorio: ${e.message}")
            emptyList() // Si hay error de internet, devolvemos una lista vacía para que no explote la app
        }
    }
}