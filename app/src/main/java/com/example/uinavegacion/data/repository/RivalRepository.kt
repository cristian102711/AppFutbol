package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.network.ApiService

class RivalRepository(private val apiService: ApiService) {

    suspend fun getRivales(): Result<List<Rival>> {
        return try {
            val rivales = apiService.getRivales()
            Result.success(rivales)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
