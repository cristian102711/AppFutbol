package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.data.network.ApiService

class EquipoRepository(private val apiService: ApiService) {

    suspend fun getEquipos(): Result<List<Equipo>> {
        return try {
            // 1. Hacemos la llamada que ahora devuelve un Response
            val response = apiService.getEquipos()

            // 2. Verificamos si el servidor respondió bien (Código 200 OK)
            if (response.isSuccessful) {
                // 3. Sacamos el cuerpo de la respuesta (la lista real)
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("La respuesta del servidor estaba vacía"))
                }
            } else {
                // Si el servidor dio error (404, 500, etc.)
                Result.failure(Exception("Error en el servidor: código ${response.code()}"))
            }
        } catch (e: Exception) {
            // Si falló la conexión (Timeout, Sin internet)
            Result.failure(e)
        }
    }
}