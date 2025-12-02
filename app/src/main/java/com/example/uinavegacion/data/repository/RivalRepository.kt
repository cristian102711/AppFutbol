package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.network.ApiService

class RivalRepository(private val apiService: ApiService) {

    suspend fun getRivales(): Result<List<Rival>> {
        return try {
            // 1. Obtenemos la respuesta completa
            val response = apiService.getRivales()

            // 2. Verificamos si fue exitosa (HTTP 200 OK)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("La lista de rivales llegó vacía"))
                }
            } else {
                // Error del servidor
                Result.failure(Exception("Error al obtener rivales: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión
            Result.failure(e)
        }
    }
}