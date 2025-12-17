package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.data.network.ApiService
import kotlinx.coroutines.delay

// --- MODO SIMULACIÓN: Usamos una lista en memoria para que la app funcione sin red ---

private val fakeJugadores = mutableListOf(
    Jugador(id = 1, nombre = "Lionel Messi", posicion = "Delantero", dorsal = 10, edad = 36, equipoId = 1),
    Jugador(id = 2, nombre = "Cristiano Ronaldo", posicion = "Delantero", dorsal = 7, edad = 39, equipoId = 2),
    Jugador(id = 3, nombre = "Neymar Jr", posicion = "Extremo", dorsal = 11, edad = 32, equipoId = 2),
    Jugador(id = 4, nombre = "Kylian Mbappé", posicion = "Delantero", dorsal = 7, edad = 25, equipoId = 1)
)
private var nextJugadorId = 5L

class JugadorRepository(private val apiService: ApiService) {

    /**
     * MODO SIMULACIÓN: Devuelve una lista falsa de jugadores después de un breve retraso.
     */
    suspend fun getJugadores(): Result<List<Jugador>> {
        delay(1000) // Simula un pequeño retraso de red
        return Result.success(fakeJugadores.toList())
    }

    /**
     * MODO SIMULACIÓN: Agrega un nuevo jugador a la lista en memoria.
     */
    suspend fun createJugador(jugador: Jugador): Result<Jugador> {
        delay(500)
        val newJugador = jugador.copy(id = nextJugadorId++)
        fakeJugadores.add(newJugador)
        return Result.success(newJugador)
    }

    /**
     * MODO SIMULACIÓN: Actualiza un jugador en la lista en memoria.
     */
    suspend fun updateJugador(jugador: Jugador): Result<Jugador> {
        delay(500)
        val index = fakeJugadores.indexOfFirst { it.id == jugador.id }
        if (index != -1) {
            fakeJugadores[index] = jugador
            return Result.success(jugador)
        }
        return Result.failure(Exception("Jugador no encontrado para actualizar"))
    }

    /**
     * MODO SIMULACIÓN: Elimina un jugador de la lista en memoria.
     */
    suspend fun deleteJugador(id: Long): Result<Boolean> {
        delay(500)
        val removed = fakeJugadores.removeIf { it.id == id }
        return if (removed) {
            Result.success(true)
        } else {
            Result.failure(Exception("Jugador no encontrado para eliminar"))
        }
    }
}
