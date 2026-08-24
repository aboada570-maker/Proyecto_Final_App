package com.example.dogostore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dogostore.ui.theme.DogoStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogoStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DogoStoreApp()
                }
            }
        }
    }
}

@Composable
fun DogoStoreApp() {

    // 1. Creamos el motor de navegación
    val navController = rememberNavController()

    //Obtener el contexto actual de la aplicación
    val context = LocalContext.current

    //Construir la base de datos y sacar el DAO
    val database = PerroDatabase.getDatabase(context)
    val dao = database.perroDao()

    val repositorio = remember { PerroRepository(dao) }

    //Creamos el viewModel usando nuestro factory personalizado para pasarle el DAO
    val perroViewModel: PerroViewModel = viewModel(factory = PerroViewModelFactory(repositorio))

    // 2. Definimos el mapa y decimos que inicie en "catalogo"
    NavHost(navController = navController, startDestination = "catalogo") {

        // --- RUTA 1: El Catálogo ---
        composable("catalogo") {
            PantallaCatalogo(navController = navController, viewModel = perroViewModel)
        }

        // --- RUTA 2: Los Detalles (Espera un parámetro llamado {id}) ---
        composable("detalles/{id}") { backStackEntry ->
            // Recuperamos el parámetro de la ruta (siempre llega como texto/String)
            val idString = backStackEntry.arguments?.getString("id")

            // Lo convertimos a número entero (Int). Si falla, ponemos 0 por defecto.
            val idInt = idString?.toIntOrNull() ?: 0

            // Llamamos a la pantalla pasándole el ID convertido
            PantallaDetalles(
                navController = navController,
                viewModel = perroViewModel,
                perroId = idInt
            )
        }
    }
}