package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.data.network.ApiService

class JugadorRepository(private val apiService: ApiService) {

    suspend fun getJugadores(): Result<List<Jugador>> {
        return try {
            val jugadores = apiService.getJugadores()
            Result.success(jugadores)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
