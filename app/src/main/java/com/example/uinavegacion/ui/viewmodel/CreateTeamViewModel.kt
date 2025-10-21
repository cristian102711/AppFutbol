package com.example.uinavegacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Define la estructura de un solo jugador.
 */
data class Player(
    val name: String,
    val email: String
)

/**
 * ViewModel para manejar la lógica de la pantalla "Crear Equipo".
 */
class CreateTeamViewModel : ViewModel() {

    // --- Estado del Formulario del Equipo ---
    var teamName by mutableStateOf(""); private set
    var teamEmail by mutableStateOf(""); private set

    // --- Estado del Formulario del Jugador (temporal) ---
    var playerName by mutableStateOf(""); private set
    var playerEmail by mutableStateOf(""); private set

    // --- Lista de Jugadores Añadidos ---
    // Usamos mutableStateListOf para que Compose reaccione a los cambios en la lista
    val players = mutableStateListOf<Player>()

    // --- Estado de la Pantalla ---
    // Controla si mostramos el formulario o la pantalla de confirmación
    var isConfirmed by mutableStateOf(false); private set

    // --- Funciones llamadas por la UI (Eventos) ---

    fun onTeamNameChanged(name: String) { teamName = name }
    fun onTeamEmailChanged(email: String) { teamEmail = email }
    fun onPlayerNameChanged(name: String) { playerName = name }
    fun onPlayerEmailChanged(email: String) { playerEmail = email }

    /**
     * Añade el jugador actual a la lista de jugadores del equipo.
     */
    fun addPlayer() {
        // Validación simple: no añadir jugadores sin nombre o email
        if (playerName.isNotBlank() && playerEmail.isNotBlank()) {
            val newPlayer = Player(name = playerName, email = playerEmail)
            players.add(newPlayer)

            // Limpia los campos del jugador
            playerName = ""
            playerEmail = ""
        }
        // (Opcional: podrías añadir un 'else' para mostrar un error)
    }

    /**
     * Confirma el equipo y cambia la vista a la pantalla de confirmación.
     */
    fun confirmTeam() {
        // Validación simple: el equipo debe tener nombre y al menos un jugador
        if (teamName.isNotBlank() && players.isNotEmpty()) {
            isConfirmed = true
        }
        // (Opcional: mostrar error si no es válido)
    }

    /**
     * Vuelve al modo de edición (botón "Editar equipo").
     */
    fun editTeam() {
        isConfirmed = false
    }

    /**
     * Resetea todo el formulario y la lista (botón "Listo").
     */
    fun resetAndFinish() {
        teamName = ""
        teamEmail = ""
        playerName = ""
        playerEmail = ""
        players.clear()
        isConfirmed = false
        // (Aquí también navegaríamos de vuelta al Home, lo veremos luego)
    }
}