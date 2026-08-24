package com.example.dogostore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perros")
data class Perro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val raza: String,
    val edad: Int,
    val descripcion: String,
    val energia: Double,
    val urlImagen: String
)