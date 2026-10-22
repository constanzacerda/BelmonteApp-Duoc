package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.ConductorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarViajeScreen(
    viajeId: String,
    viewModel: ConductorViewModel,
    navController: NavController
) {
    val viajes by viewModel.viajes.collectAsState()
    val viajeActual = viajes.find { it.id == viajeId }

    var nombrePasajero by remember { mutableStateOf(viajeActual?.nombrePasajero ?: "") }
    var direccionDestino by remember { mutableStateOf(viajeActual?.direccionDestino ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modificar Registro", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            Text("Viaje ID: $viajeId", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombrePasajero,
                onValueChange = { nombrePasajero = it },
                label = { Text("Nombre del Pasajero") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = direccionDestino,
                onValueChange = { direccionDestino = it },
                label = { Text("Dirección de Destino") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viajeActual?.let {
                        val viajeModificado = it.copy(
                            nombrePasajero = nombrePasajero,
                            direccionDestino = direccionDestino
                        )
                        viewModel.actualizarViajeModificado(viajeModificado)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Cambios y Regresar")
            }
        }
    }
}