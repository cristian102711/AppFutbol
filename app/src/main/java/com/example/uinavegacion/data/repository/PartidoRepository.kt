package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.network.ApiService

class PartidoRepository(private val apiService: ApiService) {

    // --- CORREGIDO: Ahora desempaquetamos el Response ---
    suspend fun getPartidos(): Result<List<Partido>> {
        return try {
            // 1. Obtenemos la respuesta completa (Código estado + Datos)
            val response = apiService.getPartidos()

            // 2. Verificamos si fue exitosa (HTTP 200-299)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("La lista de partidos llegó vacía"))
                }
            } else {
                // Error del servidor (404, 500, etc)
                Result.failure(Exception("Error al obtener partidos: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión
            Result.failure(e)
        }
    }

    // --- ESTA FUNCIÓN YA ESTABA CASI BIEN, SOLO LA PULIMOS ---
    suspend fun createPartido(partido: Partido): Result<Partido> {
        return try {
            val response = apiService.createPartido(partido)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("El servidor creó el partido pero no devolvió datos"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error al crear el partido: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}