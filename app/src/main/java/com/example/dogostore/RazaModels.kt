package com.example.dogostore

import com.google.gson.annotations.SerializedName

// 1. La respuesta principal: el servidor nos manda una lista dentro de la variable "data"
data class RespuestaDogApi(
    @SerializedName("data") val resultados: List<RazaRed>
)

// 2. Cada raza viene envuelta en "attributes" (formato JSON:API de dogapi.dog)
data class RazaRed(
    @SerializedName("id") val id: String,
    @SerializedName("attributes") val atributos: AtributosRaza
)

data class AtributosRaza(
    @SerializedName("name") val nombre: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("life") val vida: RangoVida,
    @SerializedName("traits") val rasgos: Rasgos,
    @SerializedName("images") val imagenes: List<ImagenRaza>
)

data class RangoVida(
    @SerializedName("min") val min: Int,
    @SerializedName("max") val max: Int
)

data class Rasgos(
    @SerializedName("energy") val energia: Int
)

data class ImagenRaza(
    @SerializedName("medium") val urlMedium: String
)