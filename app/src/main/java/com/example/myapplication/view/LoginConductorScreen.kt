package com.example.myapplication.view
import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AzulMarinoPrimary
import com.example.myapplication.ui.theme.DoradoDark
import com.example.myapplication.ui.theme.DoradoPrimary
import com.example.myapplication.ui.theme.EstadoEnCurso
import com.example.myapplication.ui.theme.EstadoPendiente

@Composable
fun LoginConductorScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var gpsPermitido by remember { mutableStateOf(false) }
    var mostrarDialogoGps by remember { mutableStateOf(true) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val fineLocation = permisos[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocation = permisos[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        gpsPermitido = fineLocation || coarseLocation
        if (!gpsPermitido) {
            Toast.makeText(context, "Debe otorgar permiso de ubicación para usar el GPS", Toast.LENGTH_LONG).show()
        }
    }

    if (mostrarDialogoGps) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoGps = false },
            icon = {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AzulMarinoPrimary, modifier = Modifier.size(36.dp))
            },
            title = {
                Text("Permiso de Ubicación", fontWeight = FontWeight.Bold, color = AzulMarinoPrimary)
            },
            text = {
                Text(
                    "Belmonte Traslados necesita acceder a su ubicación para:\n\n" +
                            "• Mostrar la distancia a los destinos\n" +
                            "• Habilitar la navegación GPS\n" +
                            "• Calcular rutas hacia los puntos de recogida y entrega\n\n" +
                            "Su ubicación solo se usa mientras la app está abierta.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoGps = false
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DoradoPrimary, contentColor = Color.Black)
                ) {
                    Text("Permitir", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoGps = false }) {
                    Text("Ahora no", color = AzulMarinoPrimary)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF9F8F6), Color(0xFFE8E0D5))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = DoradoPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Belmonte SpA",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulMarinoPrimary
                )

                Text(
                    text = "Acceso Conductor",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DoradoDark
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it; error = null },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; error = null },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = if (gpsPermitido) EstadoEnCurso else EstadoPendiente,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (gpsPermitido) "GPS activo" else "GPS pendiente",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (gpsPermitido) EstadoEnCurso else EstadoPendiente
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (usuario == "conductor" && password == "123") {
                            onLoginSuccess()
                        } else {
                            error = "Credenciales inválidas. Use: conductor / 123"
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoradoPrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Ingresar", fontWeight = FontWeight.ExtraBold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Datos ficticios - MVP Educativo",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}