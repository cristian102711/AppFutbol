package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.network.ApiService
import kotlinx.coroutines.delay

// --- MODO SIMULACIÓN: Usamos una lista en memoria para que la app funcione sin red ---

private val fakePartidos = mutableListOf(
    Partido(id = 1, fecha = "2025-07-15", resultado = "GANADO", golesFavor = 3, golesContra = 1, rivalId = 1),
    Partido(id = 2, fecha = "2025-07-22", resultado = "PERDIDO", golesFavor = 0, golesContra = 2, rivalId = 2),
    Partido(id = 3, fecha = "2025-07-29", resultado = "EMPATE", golesFavor = 2, golesContra = 2, rivalId = 3)
)
private var nextPartidoId = 4L

class PartidoRepository(private val apiService: ApiService) {

    /**
     * MODO SIMULACIÓN: Devuelve una lista falsa de partidos después de un breve retraso.
     */
    suspend fun getPartidos(): Result<List<Partido>> {
        delay(1000) // Simula un pequeño retraso de red
        return Result.success(fakePartidos.toList())
    }

    /**
     * MODO SIMULACIÓN: Agrega un nuevo partido a la lista en memoria.
     */
    suspend fun createPartido(partido: Partido): Result<Partido> {
        delay(500)
        val newPartido = partido.copy(id = nextPartidoId++)
        fakePartidos.add(newPartido)
        return Result.success(newPartido)
    }

    /**
     * MODO SIMULACIÓN: Actualiza un partido en la lista en memoria.
     */
    suspend fun updatePartido(partido: Partido): Result<Partido> {
        delay(500)
        val index = fakePartidos.indexOfFirst { it.id == partido.id }
        if (index != -1) {
            fakePartidos[index] = partido
            return Result.success(partido)
        }
        return Result.failure(Exception("Partido no encontrado para actualizar"))
    }

    /**
     * MODO SIMULACIÓN: Elimina un partido de la lista en memoria.
     */
    suspend fun deletePartido(id: Long): Result<Boolean> {
        delay(500)
        val removed = fakePartidos.removeIf { it.id == id }
        return if (removed) {
            Result.success(true)
        } else {
            Result.failure(Exception("Partido no encontrado para eliminar"))
        }
    }
}
