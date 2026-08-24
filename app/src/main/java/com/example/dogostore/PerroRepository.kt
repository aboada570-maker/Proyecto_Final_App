package com.example.dogostore

import kotlinx.coroutines.flow.Flow

// El repositorio pide el DAO (Room) en su constructor
class PerroRepository(
    private val perroDao: PerroDao
) {

    // --- OPERACIONES LOCALES (ROOM) ---

    // Obtenemos el Flow de la base de datos
    val perrosLocales: Flow<List<Perro>> = perroDao.obtenerPerros()

    suspend fun insertarPerroLocal(perro: Perro) {
        perroDao.insertarPerro(perro)
    }
}