package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.data.network.ApiService
import kotlinx.coroutines.delay

// --- MODO SIMULACIÓN: Usamos una lista en memoria para que la app funcione sin red ---

private val fakeEquipos = mutableListOf(
    Equipo(id = 1, nombre = "Los Gladiadores", entrenador = "Carlos Pérez", escudoUrl = "https://ejemplo.com/escudo1.png"),
    Equipo(id = 2, nombre = "Real Sociedad", entrenador = "Ana Gómez", escudoUrl = "https://ejemplo.com/escudo2.png"),
    Equipo(id = 3, nombre = "Titanes del Barrio", entrenador = "Luis Martínez", escudoUrl = "https://ejemplo.com/escudo3.png")
)
private var nextEquipoId = 4L

class EquipoRepository(private val apiService: ApiService) {

    /**
     * MODO SIMULACIÓN: Devuelve una lista falsa de equipos después de un breve retraso.
     */
    suspend fun getEquipos(): Result<List<Equipo>> {
        delay(1000) // Simula un pequeño retraso de red
        return Result.success(fakeEquipos.toList())
    }

    /**
     * MODO SIMULACIÓN: Agrega un nuevo equipo a la lista en memoria.
     */
    suspend fun createEquipo(equipo: Equipo): Result<Equipo> {
        delay(500)
        val newEquipo = equipo.copy(id = nextEquipoId++)
        fakeEquipos.add(newEquipo)
        return Result.success(newEquipo)
    }

    /**
     * MODO SIMULACIÓN: Actualiza un equipo en la lista en memoria.
     */
    suspend fun updateEquipo(equipo: Equipo): Result<Equipo> {
        delay(500)
        val index = fakeEquipos.indexOfFirst { it.id == equipo.id }
        if (index != -1) {
            fakeEquipos[index] = equipo
            return Result.success(equipo)
        }
        return Result.failure(Exception("Equipo no encontrado para actualizar"))
    }

    /**
     * MODO SIMULACIÓN: Elimina un equipo de la lista en memoria.
     */
    suspend fun deleteEquipo(id: Long): Result<Boolean> {
        delay(500)
        val removed = fakeEquipos.removeIf { it.id == id }
        return if (removed) {
            Result.success(true)
        } else {
            Result.failure(Exception("Equipo no encontrado para eliminar"))
        }
    }
}
