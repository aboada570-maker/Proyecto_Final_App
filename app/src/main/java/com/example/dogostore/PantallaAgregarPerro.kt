package com.example.dogostore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarPerro(navController: NavController, viewModel: PerroViewModel) {

    // Un estado local por cada campo del formulario
    var nombreInput by remember { mutableStateOf("") }
    var razaInput by remember { mutableStateOf("") }
    var edadInput by remember { mutableStateOf("") }
    var energiaInput by remember { mutableStateOf("") }
    var urlImagenInput by remember { mutableStateOf("") }
    var descripcionInput by remember { mutableStateOf("") }

    // Solo mostramos el error de "nombre obligatorio" después del primer intento de guardar
    var mostrarError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Perro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombreInput,
                onValueChange = { nombreInput = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = mostrarError && nombreInput.isBlank()
            )
            if (mostrarError && nombreInput.isBlank()) {
                Text(
                    text = "El nombre es obligatorio",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = razaInput,
                onValueChange = { razaInput = it },
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = edadInput,
                onValueChange = { edadInput = it },
                label = { Text("Edad (años)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = energiaInput,
                onValueChange = { energiaInput = it },
                label = { Text("Energía (1 a 5)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = urlImagenInput,
                onValueChange = { urlImagenInput = it },
                label = { Text("URL de la foto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcionInput,
                onValueChange = { descripcionInput = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombreInput.isBlank()) {
                        mostrarError = true
                    } else {
                        // Convertimos los campos de texto a número de forma segura, con valores por defecto si vienen vacíos
                        val nuevoPerro = Perro(
                            nombre = nombreInput,
                            raza = razaInput.ifBlank { "Sin raza registrada" },
                            edad = edadInput.toIntOrNull() ?: 0,
                            descripcion = descripcionInput.ifBlank { "Sin descripción." },
                            energia = energiaInput.toDoubleOrNull() ?: 3.0,
                            urlImagen = urlImagenInput.ifBlank {
                                "https://images.dogapi.dog/pnw8dtsqigv6u4k29h7v5q7bfnue"
                            }
                        )
                        viewModel.insertarPerro(nuevoPerro)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar Perro", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}