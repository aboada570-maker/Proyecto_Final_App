package com.example.dogostore

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. EL MENÚ: Definimos qué queremos pedirle al servidor
interface DogApi {
    @GET("breeds")
    suspend fun obtenerRazas(
        // La Dog API es gratuita y no pide api_key, a diferencia de TMDB
        @Query("page[size]") tamanoPagina: Int = 20
    ): RespuestaDogApi
}

// 2. EL MOTOR: Configuramos Retrofit para toda la app (Patrón Singleton)
object RetrofitClient {
    private const val BASE_URL = "https://dogapi.dog/api/v2/"

    val api: DogApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Le enseñamos a leer JSON
            .build()
            .create(DogApi::class.java)
    }
}