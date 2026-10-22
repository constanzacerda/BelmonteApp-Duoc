package com.example.myapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.ViajeDao
import com.example.myapplication.data.ViajeEntity
import com.example.myapplication.data.ViajeRepository
import kotlinx.coroutines.launch
import java.util.UUID

sealed class SolicitudState {
    object Idle : SolicitudState()
    object Loading : SolicitudState()
    object Success : SolicitudState()
    data class Error(val message: String) : SolicitudState()
}

class SolicitudViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViajeRepository

    init {
        val dao = AppDatabase.getInstance(application).viajeDao()
        repository = ViajeRepository(dao)
    }

    var nombre by mutableStateOf("")
    var origen by mutableStateOf("")
    var destino by mutableStateOf("")
    var vuelo by mutableStateOf("")
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")
    var fotoUri by mutableStateOf<String?>(null)

    var intentoEnviar by mutableStateOf(false)
        private set

    var state by mutableStateOf<SolicitudState>(SolicitudState.Idle)
        private set

    fun enviarSolicitud(viajeDao: ViajeDao) {
        intentoEnviar = true

        if (nombre.isBlank() || origen.isBlank() || destino.isBlank() || fecha.isBlank() || hora.isBlank()) {
            state = SolicitudState.Error("Por favor completa los campos obligatorios (*)")
            return
        }

        viewModelScope.launch {
            state = SolicitudState.Loading
            try {

                val nuevoId = "VJ-" + UUID.randomUUID().toString().substring(0, 6).uppercase()

                val nuevoViaje = ViajeEntity(
                    id = nuevoId,
                    nombrePasajero = nombre,
                    fecha = fecha,
                    hora = hora,
                    direccionOrigen = origen,
                    direccionDestino = destino,
                    numeroVuelo = if (vuelo.isBlank()) null else vuelo,
                    estado = "Pendiente",
                    fotoRefUri = fotoUri
                )


                repository.insertarViaje(nuevoViaje)

                state = SolicitudState.Success
            } catch (e: Exception) {
                state = SolicitudState.Error("Error al guardar: ${e.localizedMessage}")
            }
        }
    }
}