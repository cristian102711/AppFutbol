package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.network.ApiService

class PartidoRepository(private val apiService: ApiService) {

    suspend fun getPartidos(): Result<List<Partido>> {
        return try {
            val partidos = apiService.getPartidos()
            Result.success(partidos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPartido(partido: Partido): Result<Partido> {
        return try {
            val response = apiService.createPartido(partido)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear el partido: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
