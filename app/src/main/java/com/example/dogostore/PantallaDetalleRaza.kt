package com.example.dogostore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun PantallaDetalleRaza(navController: NavController, viewModel: PerroViewModel, razaId: String) {

    // La lista ya está descargada en el ViewModel (PantallaRazas la disparó antes),
    // así que solo buscamos la raza que coincide con el id de la ruta
    val listaRazas by viewModel.razasInternet.collectAsState()
    val raza = listaRazas.firstOrNull { it.id == razaId }

    if (raza != null) {
        val atributos = raza.atributos
        val urlFoto = atributos.imagenes.firstOrNull()?.urlMedium ?: ""

        Column(modifier = Modifier.fillMaxSize()) {

            // LA FOTO CON EFECTO DE DEGRADADO (GRADIENT)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AsyncImage(
                    model = urlFoto,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onError = { state ->
                        android.util.Log.e("DogoStore", "Error de Coil: ${state.result.throwable.message}")
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                ),
                                startY = 300f
                            )
                        )
                )
            }

            // EL CONTENIDO TEXTUAL
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp)
            ) {
                Text(text = atributos.nombre, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    text = "Vive entre ${atributos.vida.min} y ${atributos.vida.max} años",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "⚡ Energía ${atributos.rasgos.energia}/5",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Descripción", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = atributos.descripcion, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Volver a Razas del Mundo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    } else {
        // Mostrar un indicador mientras se ubica la raza (o si aún no ha terminado de bajar la lista)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}