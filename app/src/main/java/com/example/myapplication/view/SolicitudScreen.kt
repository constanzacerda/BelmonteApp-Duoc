package com.example.myapplication.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.SolicitudState
import com.example.myapplication.viewmodel.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudScreen(viewModel: SolicitudViewModel) {
    val context = LocalContext.current

    // Launcher para pedir permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher para capturar foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) viewModel.fotoUri = "uri_temporal_foto"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Formulario de Pasajero", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.origen,
                onValueChange = { viewModel.origen = it },
                label = { Text("Punto de Origen") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.destino,
                onValueChange = { viewModel.destino = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.vuelo,
                onValueChange = { viewModel.vuelo = it },
                label = { Text("Número de vuelo (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { viewModel.fecha = "12/10/2026" }) {
                    Text(if (viewModel.fecha.isEmpty()) "Seleccionar Fecha" else viewModel.fecha)
                }
                OutlinedButton(onClick = { viewModel.hora = "14:30" }) {
                    Text(if (viewModel.hora.isEmpty()) "Seleccionar Hora" else viewModel.hora)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
                cameraLauncher.launch(null)
            }) {
                Text(if (viewModel.fotoUri == null) "Tomar Foto Referencia" else "Foto Capturada ✓")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.enviarSolicitud() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.state !is SolicitudState.Loading
            ) {
                Text("Confirmar Solicitud")
            }

            when (val state = viewModel.state) {
                is SolicitudState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                is SolicitudState.Success -> Text("¡Solicitud enviada con éxito!", color = MaterialTheme.colorScheme.primary)
                is SolicitudState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                else -> {}
            }
        }
    }
}