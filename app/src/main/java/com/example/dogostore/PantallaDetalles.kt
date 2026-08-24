package com.example.dogostore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun PantallaDetalles(navController: NavController, viewModel: PerroViewModel, perroId: Int) {

    // Creamos un estado mutable para almacenar el perro obtenido del ViewModel
    var perroState by remember { mutableStateOf<Perro?>(null) }

    LaunchedEffect(perroId) {
        perroState = viewModel.obtenerPerroPorId(perroId)
    }

    // Guardamos el perro en una variable para no tener que acceder a la propiedad mutableStateOf cada vez
    val perro = perroState

    if (perro != null) {
        Column(modifier = Modifier.fillMaxSize()) {

            // LA FOTO CON EFECTO DE DEGRADADO (GRADIENT)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // 1. La imagen al fondo con LOG de Errores (Log.e)
                AsyncImage(
                    model = perro.urlImagen,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onError = { state ->
                        android.util.Log.e("DogoStore", "Error de Coil: ${state.result.throwable.message}")
                    }
                )

                // 2. El degradado superpuesto
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background // Se funde con el fondo de la app
                                ),
                                startY = 300f // El degradado empieza en la mitad inferior
                            )
                        )
                )
            }

            // EL CONTENIDO TEXTUAL
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    // Hacemos que la columna se desplace hacia arriba sobre la imagen
                    .offset(y = (-40).dp)
            ) {
                Text(text = perro.nombre, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    text = "${perro.edad} años • Raza ${perro.raza}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Descripción", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = perro.descripcion, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al final

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Volver al Catálogo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }else {
        // Mostrar un indicador mientras se obtiene el perro
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}