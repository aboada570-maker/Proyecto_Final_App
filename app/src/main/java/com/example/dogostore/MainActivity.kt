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

    //Obtener el contexto actual de la aplicación
    val context = LocalContext.current

    //Construir la base de datos y sacar el DAO
    val database = PerroDatabase.getDatabase(context)
    val dao = database.perroDao()

    val repositorio = remember { PerroRepository(dao) }

    //Creamos el viewModel usando nuestro factory personalizado para pasarle el DAO
    val perroViewModel: PerroViewModel = viewModel(factory = PerroViewModelFactory(repositorio))

    PantallaCatalogo(viewModel = perroViewModel)
}