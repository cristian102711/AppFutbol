package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.network.ApiService
import kotlinx.coroutines.delay

// --- MODO SIMULACIÓN: Usamos una lista en memoria para que la app funcione sin red ---

// Lista mutable para simular una base de datos local
private val fakeRivals = mutableListOf(
    Rival(id = 1, nombre = "Los Audaces FC"),
    Rival(id = 2, nombre = "Atlético Fénix"),
    Rival(id = 3, nombre = "Deportivo Halcón"),
    Rival(id = 4, nombre = "Unión Barrial")
)
private var nextRivalId = 5L

class RivalRepository(private val apiService: ApiService) {

    /**
     * MODO SIMULACIÓN: Devuelve una lista falsa de rivales después de un breve retraso.
     */
    suspend fun getRivales(): Result<List<Rival>> {
        delay(1000) // Simula un pequeño retraso de red
        return Result.success(fakeRivals.toList()) // Devuelve una copia de la lista
    }

    /**
     * MODO SIMULACIÓN: Agrega un nuevo rival a la lista en memoria.
     */
    suspend fun createRival(rival: Rival): Result<Rival> {
        delay(500)
        val newRival = rival.copy(id = nextRivalId++)
        fakeRivals.add(newRival)
        return Result.success(newRival)
    }

    /**
     * MODO SIMULACIÓN: Actualiza un rival en la lista en memoria.
     */
    suspend fun updateRival(rival: Rival): Result<Rival> {
        delay(500)
        val index = fakeRivals.indexOfFirst { it.id == rival.id }
        if (index != -1) {
            fakeRivals[index] = rival
            return Result.success(rival)
        }
        return Result.failure(Exception("Rival no encontrado para actualizar"))
    }

    /**
     * MODO SIMULACIÓN: Elimina un rival de la lista en memoria.
     */
    suspend fun deleteRival(id: Long): Result<Boolean> {
        delay(500)
        val removed = fakeRivals.removeIf { it.id == id }
        return if (removed) {
            Result.success(true)
        } else {
            Result.failure(Exception("Rival no encontrado para eliminar"))
        }
    }
}
