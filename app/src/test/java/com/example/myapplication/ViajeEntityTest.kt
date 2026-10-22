package com.example.myapplication

import com.example.myapplication.data.ViajeEntity
import com.example.myapplication.model.Viaje
import org.junit.Assert.*
import org.junit.Test

class ViajeEntityTest {

    @Test
    fun crearViaje_conDatosCompletos_esValido() {
        val viaje = ViajeEntity(
            id = "VJ-001",
            nombrePasajero = "Carlos Mendoza",
            fecha = "10/10/2026",
            hora = "08:30",
            direccionOrigen = "Aeropuerto SCL",
            direccionDestino = "Hotel Marriott",
            latitudOrigen = -33.3928,
            longitudOrigen = -70.7856,
            latitudDestino = -33.3994,
            longitudDestino = -70.5788,
            numeroVuelo = "LA-402",
            estado = "Pendiente"
        )

        assertEquals("VJ-001", viaje.id)
        assertEquals("Carlos Mendoza", viaje.nombrePasajero)
        assertEquals("Pendiente", viaje.estado)
        assertEquals("LA-402", viaje.numeroVuelo)
        assertEquals(-33.3928, viaje.latitudOrigen, 0.001)
    }

    @Test
    fun crearViaje_sinVuelo_vueloEsNull() {
        val viaje = ViajeEntity(
            id = "VJ-002",
            nombrePasajero = "Ana Torres",
            fecha = "10/10/2026",
            hora = "09:15",
            direccionOrigen = "Oficina Central",
            direccionDestino = "Aeropuerto SCL"
        )

        assertNull(viaje.numeroVuelo)
        assertEquals("Pendiente", viaje.estado)
    }

    @Test
    fun crearViaje_estadosPosibles_sonCorrectos() {
        val estados = listOf("Pendiente", "Asignado", "En Curso", "Finalizado")

        estados.forEach { estado ->
            val viaje = ViajeEntity(
                id = "VJ-TEST",
                nombrePasajero = "Test",
                fecha = "10/10/2026",
                hora = "10:00",
                direccionOrigen = "A",
                direccionDestino = "B",
                estado = estado
            )
            assertEquals(estado, viaje.estado)
        }
    }

    @Test
    fun crearViaje_coordenadasPorDefecto_sonCero() {
        val viaje = ViajeEntity(
            id = "VJ-003",
            nombrePasajero = "Test",
            fecha = "10/10/2026",
            hora = "10:00",
            direccionOrigen = "A",
            direccionDestino = "B"
        )

        assertEquals(0.0, viaje.latitudOrigen, 0.001)
        assertEquals(0.0, viaje.longitudOrigen, 0.001)
        assertEquals(0.0, viaje.latitudDestino, 0.001)
        assertEquals(0.0, viaje.longitudDestino, 0.001)
    }

    @Test
    fun flujoEstados_pendienteAFinalizado_esSecuencial() {
        val flujo = mapOf(
            "Pendiente" to "Asignado",
            "Asignado" to "En Curso",
            "En Curso" to "Finalizado"
        )

        var estadoActual = "Pendiente"
        val secuencia = mutableListOf(estadoActual)

        while (flujo.containsKey(estadoActual)) {
            estadoActual = flujo[estadoActual]!!
            secuencia.add(estadoActual)
        }

        assertEquals(listOf("Pendiente", "Asignado", "En Curso", "Finalizado"), secuencia)
    }

    @Test
    fun modeloUnificado_entityAViaje_convierteCorrectamente() {
        val entity = ViajeEntity(
            id = "VJ-010",
            nombrePasajero = "Pedro Lopez",
            fecha = "15/10/2026",
            hora = "14:00",
            direccionOrigen = "Hotel A",
            direccionDestino = "Aeropuerto SCL",
            latitudOrigen = -33.4264,
            longitudOrigen = -70.6133,
            latitudDestino = -33.3928,
            longitudDestino = -70.7856,
            numeroVuelo = "LA-100",
            estado = "Asignado",
            fotoRefUri = "uri_foto_test"
        )

        val viaje = entity.toViaje()

        assertEquals(entity.id, viaje.id)
        assertEquals(entity.nombrePasajero, viaje.nombrePasajero)
        assertEquals(entity.fecha, viaje.fecha)
        assertEquals(entity.hora, viaje.hora)
        assertEquals(entity.direccionOrigen, viaje.direccionOrigen)
        assertEquals(entity.direccionDestino, viaje.direccionDestino)
        assertEquals(entity.latitudOrigen, viaje.latitudOrigen, 0.001)
        assertEquals(entity.longitudOrigen, viaje.longitudOrigen, 0.001)
        assertEquals(entity.latitudDestino, viaje.latitudDestino, 0.001)
        assertEquals(entity.longitudDestino, viaje.longitudDestino, 0.001)
        assertEquals(entity.numeroVuelo, viaje.numeroVuelo)
        assertEquals(entity.estado, viaje.estado)
        assertEquals(entity.fotoRefUri, viaje.fotoRefUri)
    }

    @Test
    fun modeloUnificado_viajeAEntity_convierteCorrectamente() {
        val viaje = Viaje(
            id = "VJ-020",
            nombrePasajero = "Laura Soto",
            fecha = "20/10/2026",
            hora = "09:30",
            direccionOrigen = "Oficina B",
            direccionDestino = "Aeropuerto SCL",
            latitudOrigen = -33.4100,
            longitudOrigen = -70.5900,
            latitudDestino = -33.3928,
            longitudDestino = -70.7856,
            numeroVuelo = "IB-500",
            estado = "Pendiente",
            fotoRefUri = null
        )

        val entity = ViajeEntity.fromViaje(viaje)

        assertEquals(viaje.id, entity.id)
        assertEquals(viaje.nombrePasajero, entity.nombrePasajero)
        assertEquals(viaje.estado, entity.estado)
        assertEquals(viaje.latitudOrigen, entity.latitudOrigen, 0.001)
        assertEquals(viaje.longitudOrigen, entity.longitudOrigen, 0.001)
        assertEquals(viaje.latitudDestino, entity.latitudDestino, 0.001)
        assertEquals(viaje.longitudDestino, entity.longitudDestino, 0.001)
        assertNull(entity.fotoRefUri)
    }

    @Test
    fun modeloUnificado_conversionIdaYVuelta_mantieneIntegridad() {
        val entityOriginal = ViajeEntity(
            id = "VJ-030",
            nombrePasajero = "Test Ida Vuelta",
            fecha = "25/10/2026",
            hora = "16:00",
            direccionOrigen = "Punto A",
            direccionDestino = "Punto B",
            latitudOrigen = -33.4167,
            longitudOrigen = -70.6000,
            latitudDestino = -33.4050,
            longitudDestino = -70.5700,
            estado = "En Curso"
        )

        val viajeIntermedio = entityOriginal.toViaje()
        val entityReconstruida = ViajeEntity.fromViaje(viajeIntermedio)

        assertEquals(entityOriginal.id, entityReconstruida.id)
        assertEquals(entityOriginal.nombrePasajero, entityReconstruida.nombrePasajero)
        assertEquals(entityOriginal.estado, entityReconstruida.estado)
        assertEquals(entityOriginal.latitudOrigen, entityReconstruida.latitudOrigen, 0.001)
        assertEquals(entityOriginal.longitudOrigen, entityReconstruida.longitudOrigen, 0.001)
        assertEquals(entityOriginal.latitudDestino, entityReconstruida.latitudDestino, 0.001)
        assertEquals(entityOriginal.longitudDestino, entityReconstruida.longitudDestino, 0.001)
    }

    @Test
    fun crearViaje_conFotoRef_fotoNoEsNull() {
        val viaje = ViajeEntity(
            id = "VJ-040",
            nombrePasajero = "Test Foto",
            fecha = "10/10/2026",
            hora = "10:00",
            direccionOrigen = "A",
            direccionDestino = "B",
            fotoRefUri = "content://media/foto123"
        )

        assertNotNull(viaje.fotoRefUri)
        assertEquals("content://media/foto123", viaje.fotoRefUri)
    }


    @Test
    fun georreferenciacion_coordenadasSantiago_sonValidas() {
        val viaje = ViajeEntity(
            id = "VJ-GEO",
            nombrePasajero = "Test Geo",
            fecha = "10/10/2026",
            hora = "10:00",
            direccionOrigen = "Aeropuerto SCL",
            direccionDestino = "Hotel Marriott",
            latitudOrigen = -33.3928,
            longitudOrigen = -70.7856,
            latitudDestino = -33.3994,
            longitudDestino = -70.5788
        )


        assertTrue(viaje.latitudOrigen >= -90 && viaje.latitudOrigen <= 90)
        assertTrue(viaje.longitudOrigen >= -180 && viaje.longitudOrigen <= 180)
        assertTrue(viaje.latitudDestino >= -90 && viaje.latitudDestino <= 90)
        assertTrue(viaje.longitudDestino >= -180 && viaje.longitudDestino <= 180)


        assertTrue(viaje.latitudOrigen != 0.0)
        assertTrue(viaje.longitudOrigen != 0.0)


        val viajeApi = viaje.toViaje()
        assertEquals(viaje.latitudOrigen, viajeApi.latitudOrigen, 0.0001)
        assertEquals(viaje.longitudOrigen, viajeApi.longitudOrigen, 0.0001)
        assertEquals(viaje.latitudDestino, viajeApi.latitudDestino, 0.0001)
        assertEquals(viaje.longitudDestino, viajeApi.longitudDestino, 0.0001)
    }
}
