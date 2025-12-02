package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.data.network.ApiService

class JugadorRepository(private val apiService: ApiService) {

    suspend fun getJugadores(): Result<List<Jugador>> {
        return try {
            // 1. Recibimos la respuesta completa del servidor
            val response = apiService.getJugadores()

            // 2. Verificamos si fue exitosa (Código 200)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("La lista de jugadores llegó vacía"))
                }
            } else {
                // Error del servidor (404, 500)
                Result.failure(Exception("Error al obtener jugadores: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión (Timeout, Sin internet)
            Result.failure(e)
        }
    }
}