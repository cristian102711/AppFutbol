package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.data.network.ApiService

class EquipoRepository(private val apiService: ApiService) {

    suspend fun getEquipos(): Result<List<Equipo>> {
        return try {
            val equipos = apiService.getEquipos()
            Result.success(equipos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
