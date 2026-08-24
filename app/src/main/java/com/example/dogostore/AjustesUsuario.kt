package com.example.dogostore

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

val Context.dataStore by preferencesDataStore(name = "ajustes_dogostore")

class AjustesUsuario(private val context: Context) {

    companion object {
        val NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
    }

    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { preferencias ->
            preferencias[NOMBRE_USUARIO] = nombre
        }
    }

    val nombreUsuarioFlow: Flow<String> = context.dataStore.data.map { preferencias ->
        preferencias[NOMBRE_USUARIO] ?: "Invitado"
    }

    fun obtenerArchivoFotoPerfil(): File {
        return File(context.filesDir, "foto_perfil.jpg")
    }

    fun guardarFotoDesdeCamara(bitmap: Bitmap) {
        // Guardamos la foto en el almacenamiento interno de la app
        val archivo = obtenerArchivoFotoPerfil()
        // Creamos el archivo si no existe
        FileOutputStream(archivo).use{ outputStream ->
            // Comprimimos el bitmap en formato JPEG y lo escribimos en el archivo
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
    }


    fun guardarFotoDesdeGaleria(uri: Uri){
        // Guardamos la foto en el almacenamiento interno de la app
        val archivo = obtenerArchivoFotoPerfil()

        // Abrimos un InputStream desde el URI y copiamos su contenido al archivo de la app
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(archivo).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}