package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner // Import para el botón QR
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // Import necesario para los botones Modificar/Eliminar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.ViajeEntity
import com.example.myapplication.ui.theme.AzulMarinoPrimary
import com.example.myapplication.ui.theme.AzulMarinoVariant
import com.example.myapplication.ui.theme.DoradoDark
import com.example.myapplication.ui.theme.DoradoPrimary
import com.example.myapplication.ui.theme.EstadoAsignado
import com.example.myapplication.ui.theme.EstadoEnCurso
import com.example.myapplication.ui.theme.EstadoFinalizado
import com.example.myapplication.ui.theme.EstadoPendiente
import com.example.myapplication.viewmodel.ConductorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutasConductorScreen(viewModel: ConductorViewModel, navController: NavController) {
    val viajes by viewModel.viajes.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val gpsActivo by viewModel.gpsActivo.collectAsState()
    val ubicacion by viewModel.ubicacionConductor.collectAsState()
    val conectadoApi by viewModel.conectadoApi.collectAsState()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Belmonte Traslados",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Panel del Conductor",
                                style = MaterialTheme.typography.bodySmall,
                                color = AzulMarinoVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = if (gpsActivo) Icons.Default.GpsFixed else Icons.Default.GpsOff,
                                contentDescription = if (gpsActivo) "GPS activo" else "GPS inactivo",
                                tint = if (gpsActivo) EstadoEnCurso else EstadoPendiente,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))

                            Icon(
                                imageVector = if (conectadoApi) Icons.Default.Cloud else Icons.Default.CloudOff,
                                contentDescription = if (conectadoApi) "API conectada" else "Modo sin conexión",
                                tint = if (conectadoApi) EstadoEnCurso else EstadoPendiente,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE8E0D5),
                    titleContentColor = AzulMarinoPrimary
                ),
                actions = {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Conductor",
                        tint = AzulMarinoPrimary,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {

                FloatingActionButton(
                    onClick = { navController.navigate("escaner_qr") },
                    containerColor = Color.Black,
                    contentColor = DoradoPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Leer QR")
                }

                FloatingActionButton(
                    onClick = {
                        viewModel.reintentarGps()
                        viewModel.sincronizarConApi()
                        Toast.makeText(context, "Sincronizando GPS y API…", Toast.LENGTH_SHORT).show()
                    },
                    containerColor = DoradoPrimary,
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Sync, contentDescription = "Sincronizar")
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF9F8F6), Color(0xFFE8E0D5))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                FiltrosEstado(
                    filtroActual = filtroActual,
                    onFiltroSeleccionado = { viewModel.cambiarFiltro(it) }
                )

                ResumenViajes(viajes)

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DoradoPrimary)
                    }
                } else if (viajes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.DirectionsCar,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = AzulMarinoPrimary.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No hay viajes para mostrar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = AzulMarinoPrimary.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viajes, key = { it.id }) { viaje ->
                            val distancia = viewModel.distanciaAlDestino(viaje)
                            TarjetaViaje(
                                viaje = viaje,
                                distanciaKm = distancia,
                                onAvanzarEstado = { viewModel.avanzarEstado(viaje) },
                                onNavegar = { abrirNavegacion(context, viaje) },
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FiltrosEstado(filtroActual: String, onFiltroSeleccionado: (String) -> Unit) {
    val filtros = listOf("Todos", "Pendiente", "Asignado", "En Curso", "Finalizado")

    ScrollableTabRow(
        selectedTabIndex = filtros.indexOf(filtroActual).coerceAtLeast(0),
        containerColor = Color.Transparent,
        contentColor = AzulMarinoPrimary,
        edgePadding = 16.dp
    ) {
        filtros.forEach { filtro ->
            Tab(
                selected = filtroActual == filtro,
                onClick = { onFiltroSeleccionado(filtro) },
                text = {
                    Text(
                        text = filtro,
                        fontWeight = if (filtroActual == filtro) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

@Composable
fun ResumenViajes(viajes: List<ViajeEntity>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val pendientes = viajes.count { it.estado == "Pendiente" }
        val asignados = viajes.count { it.estado == "Asignado" }
        val enCurso = viajes.count { it.estado == "En Curso" }
        val finalizados = viajes.count { it.estado == "Finalizado" }

        ContadorEstado("Pendientes", pendientes, EstadoPendiente)
        ContadorEstado("Asignados", asignados, EstadoAsignado)
        ContadorEstado("En Curso", enCurso, EstadoEnCurso)
        ContadorEstado("Finalizados", finalizados, EstadoFinalizado)
    }
}

@Composable
fun ContadorEstado(label: String, cantidad: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$cantidad",
                fontWeight = FontWeight.Bold,
                color = color,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AzulMarinoPrimary
        )
    }
}

@Composable
fun TarjetaViaje(
    viaje: ViajeEntity,
    distanciaKm: Double? = null,
    onAvanzarEstado: () -> Unit,
    onNavegar: () -> Unit,
    navController: NavController,
    viewModel: ConductorViewModel
) {
    val colorEstado = when (viaje.estado) {
        "Pendiente" -> EstadoPendiente
        "Asignado" -> EstadoAsignado
        "En Curso" -> EstadoEnCurso
        "Finalizado" -> EstadoFinalizado
        else -> MaterialTheme.colorScheme.outline
    }

    val animatedColor by animateColorAsState(targetValue = colorEstado, label = "estadoColor")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viaje.id,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = animatedColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = viaje.estado,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = animatedColor,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = AzulMarinoVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = viaje.nombrePasajero,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = DoradoDark, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${viaje.fecha} a las ${viaje.hora}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            viaje.numeroVuelo?.let { vuelo ->
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Flight, contentDescription = null, tint = DoradoDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vuelo: $vuelo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.MyLocation, contentDescription = null, tint = EstadoEnCurso, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Origen", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = viaje.direccionOrigen,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Destino", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = viaje.direccionDestino,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            distanciaKm?.let { km ->
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.GpsFixed, contentDescription = null, tint = EstadoAsignado, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Distancia: %.1f km".format(km),
                        style = MaterialTheme.typography.labelMedium,
                        color = EstadoAsignado,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (viaje.estado != "Finalizado") {
                    OutlinedButton(
                        onClick = onNavegar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AzulMarinoVariant
                        )
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Navegar")
                    }
                }

                if (viaje.estado != "Finalizado") {
                    Button(
                        onClick = onAvanzarEstado,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorEstado,
                            contentColor = Color.White
                        )
                    ) {
                        val textoBoton = when (viaje.estado) {
                            "Pendiente" -> "Asignar"
                            "Asignado" -> "Iniciar"
                            "En Curso" -> "Finalizar"
                            else -> ""
                        }
                        Icon(
                            if (viaje.estado == "En Curso") Icons.Default.CheckCircle else Icons.Default.DirectionsCar,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(textoBoton)
                    }
                } else {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        color = EstadoFinalizado.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EstadoFinalizado,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Completado",
                                color = EstadoFinalizado,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("editar_viaje/${viaje.id}") },
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DoradoDark),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Modificar", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { viewModel.eliminarViajeLocal(viaje) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFEBEE),
                        contentColor = Color(0xFFD32F2F)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Eliminar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun abrirNavegacion(context: Context, viaje: ViajeEntity) {
    val origen = if (viaje.latitudOrigen != 0.0) "${viaje.latitudOrigen},${viaje.longitudOrigen}" else Uri.encode(viaje.direccionOrigen)
    val destino = if (viaje.latitudDestino != 0.0) "${viaje.latitudDestino},${viaje.longitudDestino}" else Uri.encode(viaje.direccionDestino)

    val uriMaps = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$origen&destination=$destino&travelmode=driving")
    val mapIntent = Intent(Intent.ACTION_VIEW, uriMaps)
    mapIntent.setPackage("com.google.android.apps.maps")

    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        try {
            val uriWaze = if (viaje.latitudDestino != 0.0) {
                "waze://?ll=${viaje.latitudDestino},${viaje.longitudDestino}&navigate=yes"
            } else {
                "waze://?q=${Uri.encode(viaje.direccionDestino)}&navigate=yes"
            }
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriWaze)))
        } catch (e: Exception) {
            Toast.makeText(context, "No se encontró app de navegación. Abriendo navegador.", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(Intent.ACTION_VIEW, uriMaps))
        }
    }

}
