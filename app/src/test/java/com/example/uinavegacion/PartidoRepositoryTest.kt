package com.example.uinavegacion

import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.network.ApiService
import com.example.uinavegacion.data.repository.PartidoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class PartidoRepositoryTest {

    private lateinit var mockApiService: ApiService
    private lateinit var partidoRepository: PartidoRepository

    @Before
    fun setup() {
        mockApiService = mock(ApiService::class.java)
        partidoRepository = PartidoRepository(mockApiService)
    }

    @Test
    fun `getPartidos retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange: preparar mock
        val partidoList = listOf(
            Partido(1, "2024-12-16", 1L, "GANADO", 3, 1),
            Partido(2, "2024-12-10", 2L, "EMPATADO", 2, 2)
        )
        `when`(mockApiService.getPartidos()).thenReturn(
            Response.success(partidoList)
        )

        // Act: ejecutar
        val result = partidoRepository.getPartidos()

        // Assert: verificar
        assertTrue(result.isSuccess)
        result.onSuccess { partidos ->
            assertTrue(partidos.size == 2)
            assertTrue(partidos[0].golesFavor == 3)
        }
    }

    @Test
    fun `createPartido retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange
        val newPartido = Partido(
            id = 0,
            fecha = "2024-12-20",
            rivalId = 3L,
            resultado = "GANADO",
            golesFavor = 2,
            golesContra = 1
        )
        val createdPartido = newPartido.copy(id = 3)
        `when`(mockApiService.createPartido(newPartido)).thenReturn(
            Response.success(createdPartido)
        )

        // Act
        val result = partidoRepository.createPartido(newPartido)

        // Assert
        assertTrue(result.isSuccess)
        result.onSuccess { partido ->
            assertTrue(partido.id == 3L)
            assertTrue(partido.golesFavor == 2)
        }
    }

    @Test
    fun `updatePartido retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange
        val updatedPartido = Partido(
            id = 1,
            fecha = "2024-12-16",
            rivalId = 1L,
            resultado = "PERDIDO",
            golesFavor = 1,
            golesContra = 2
        )
        `when`(mockApiService.updatePartido(1L, updatedPartido)).thenReturn(
            Response.success(updatedPartido)
        )

        // Act
        val result = partidoRepository.updatePartido(1L, updatedPartido)

        // Assert
        assertTrue(result.isSuccess)
        result.onSuccess { partido ->
            assertTrue(partido.resultado == "PERDIDO")
        }
    }

    @Test
    fun `deletePartido retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange
        `when`(mockApiService.deletePartido(1L)).thenReturn(
            Response.success(null)
        )

        // Act
        val result = partidoRepository.deletePartido(1L)

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getPartidos retorna Failure cuando API retorna error 500`() = runBlocking {
        // Arrange
        `when`(mockApiService.getPartidos()).thenReturn(
            Response.error(500, okhttp3.ResponseBody.create(null, "Server Error"))
        )

        // Act
        val result = partidoRepository.getPartidos()

        // Assert
        assertTrue(result.isFailure)
    }
}

