package com.example.dogostore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(viewModel: PerroViewModel) {

    // Observamos la lista de perros desde el ViewModel
    val listaPerros by viewModel.perros.collectAsState()

    // SCAFFOLD nos da una estructura profesional con Barra Superior
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DogoStore", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },

        //Agregar un boton para insertar datos
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //Insertar un perro de ejemplo
                    val nuevoPerro = Perro(
                        nombre = "Hachiko",
                        raza = "Akita",
                        edad = 5,
                        descripcion = "El Akita es una raza grande y musculosa originaria de Japón. Son conocidos por su lealtad y valentía.",
                        energia = 3.0,
                        urlImagen = "https://images.dogapi.dog/pnw8dtsqigv6u4k29h7v5q7bfnue"
                    )
                    viewModel.insertarPerro(nuevoPerro)
                }
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
                    ItemPerro(perro = perro)
                }
            }
        }
    }
}

@Composable
fun ItemPerro(perro: Perro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
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