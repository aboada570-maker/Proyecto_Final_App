package com.example.dogostore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(navController: NavController, viewModel: PerroViewModel) {

    // Observamos la lista de perros desde el ViewModel
    val listaPerros by viewModel.perros.collectAsState()

    // Obtenemos el contexto actual para poder usarlo en AjustesUsuario
    val context = LocalContext.current
    // Obtenemos el nombre de usuario desde AjustesUsuario
    val ajustesUsuario = remember { AjustesUsuario(context) }
    // Observamos el flujo de nombre de usuario y lo convertimos en un estado para que Compose lo observe
    val nombreUsuario by ajustesUsuario.nombreUsuarioFlow.collectAsState(initial = "Cargando...")

    // SCAFFOLD nos da una estructura profesional con Barra Superior
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hola $nombreUsuario", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("razas") }) {
                        Icon(Icons.Default.Pets, contentDescription = "Razas del Mundo")
                    }

                    IconButton(onClick = { navController.navigate("ajustes") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        },

        //Agregar un boton para ir al formulario de registro
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregar") }
            )
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Perro")
            }
        }

    ) { paddingValues -> // Padding automático para no tapar la barra

        //Mensaje si la base de datos esta vacia
        if (listaPerros.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No hay perros en el catálogo. Agrega uno usando el botón +",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) } // Espacio inicial
                items(listaPerros) { perro ->
                    ItemPerro(perro = perro, navController = navController)
                }
            }
        }
    }
}

@Composable
fun ItemPerro(perro: Perro, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { navController.navigate("detalles/${perro.id}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = perro.urlImagen,
                contentDescription = "Foto de ${perro.nombre}",
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop,
                onError = { state ->
                    android.util.Log.e("DogoStore", "Error cargando catálogo: ${state.result.throwable.message}")
                }
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = perro.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = perro.raza, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "⚡ ${perro.energia}/5",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}