package com.example.uinavegacion

import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.network.ApiService
import com.example.uinavegacion.data.repository.RivalRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class RivalRepositoryTest {

    private lateinit var mockApiService: ApiService
    private lateinit var rivalRepository: RivalRepository

    @Before
    fun setup() {
        mockApiService = mock(ApiService::class.java)
        rivalRepository = RivalRepository(mockApiService)
    }

    @Test
    fun `getRivales retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange: preparar mock
        val rivalList = listOf(
            Rival(1, "Equipo A"),
            Rival(2, "Equipo B")
        )
        `when`(mockApiService.getRivales()).thenReturn(
            Response.success(rivalList)
        )

        // Act: ejecutar
        val result = rivalRepository.getRivales()

        // Assert: verificar
        assertTrue(result.isSuccess)
        result.onSuccess { rivals ->
            assertTrue(rivals.size == 2)
        }
    }

    @Test
    fun `createRival retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange
        val newRival = Rival(3, "Rival Nuevo")
        `when`(mockApiService.createRival(newRival)).thenReturn(
            Response.success(newRival)
        )

        // Act
        val result = rivalRepository.createRival(newRival)

        // Assert
        assertTrue(result.isSuccess)
        result.onSuccess { rival ->
            assertTrue(rival.nombre == "Rival Nuevo")
        }
    }

    @Test
    fun `updateRival retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange
        val updatedRival = Rival(1, "Rival Actualizado")
        `when`(mockApiService.updateRival(1L, updatedRival)).thenReturn(
            Response.success(updatedRival)
        )

        // Act
        val result = rivalRepository.updateRival(1L, updatedRival)

        // Assert
        assertTrue(result.isSuccess)
        result.onSuccess { rival ->
            assertTrue(rival.nombre == "Rival Actualizado")
        }
    }

    @Test
    fun `deleteRival retorna Success cuando API responde exitosamente`() = runBlocking {
        // Arrange
        `when`(mockApiService.deleteRival(1L)).thenReturn(
            Response.success(null)
        )

        // Act
        val result = rivalRepository.deleteRival(1L)

        // Assert
        assertTrue(result.isSuccess)
        result.onSuccess { success ->
            assertTrue(success)
        }
    }

    @Test
    fun `getRivales retorna Failure cuando API falla`() = runBlocking {
        // Arrange
        `when`(mockApiService.getRivales()).thenReturn(
            Response.error(500, okhttp3.ResponseBody.create(null, ""))
        )

        // Act
        val result = rivalRepository.getRivales()

        // Assert
        assertTrue(result.isFailure)
    }
}

