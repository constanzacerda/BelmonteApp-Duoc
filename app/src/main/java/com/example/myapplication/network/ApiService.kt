package com.example.myapplication.network

import com.example.myapplication.model.Viaje
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class CambioEstadoRequest(
    val estado: String
)

interface ApiService {

    @GET("api/viajes")
    suspend fun obtenerViajesConductor(): List<Viaje>

    @POST("api/viajes")
    suspend fun enviarSolicitud(@Body viaje: Viaje): Viaje

    @PUT("api/viajes/{id}/estado")
    suspend fun actualizarEstadoViaje(
        @Path("id") id: String,
        @Body request: CambioEstadoRequest
    ): Viaje
}