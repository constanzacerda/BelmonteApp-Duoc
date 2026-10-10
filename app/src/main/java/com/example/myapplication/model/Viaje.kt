package com.example.myapplication.model

data class Viaje(
    val id: String = "",
    val nombrePasajero: String,
    val fecha: String,
    val hora: String,
    val direccionOrigen: String,
    val direccionDestino: String,
    val numeroVuelo: String? = null,
    val estado: String = "Pendiente",
    val fotoRefUri: String? = null
)