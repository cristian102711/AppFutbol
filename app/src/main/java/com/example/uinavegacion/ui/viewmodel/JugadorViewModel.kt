package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.data.repository.JugadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para representar el estado de la UI de la lista de jugadores
data class JugadorUiState(
    val jugadores: List<Jugador> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class JugadorViewModel(private val jugadorRepository: JugadorRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(JugadorUiState())
    val uiState: StateFlow<JugadorUiState> = _uiState.asStateFlow()

    init {
        loadJugadores()
    }

    fun loadJugadores() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = jugadorRepository.getJugadores()
            result.onSuccess { jugadores ->
                _uiState.update {
                    it.copy(isLoading = false, jugadores = jugadores)
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar jugadores: ${throwable.message}")
                }
            }
        }
    }

    // --- Nuevas acciones CRUD ---

    fun createJugador(jugador: Jugador) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = jugadorRepository.createJugador(jugador)
            result.onSuccess {
                loadJugadores()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al crear jugador: ${throwable.message}") }
            }
        }
    }

    fun updateJugador(jugador: Jugador) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = jugadorRepository.updateJugador(jugador)
            result.onSuccess {
                loadJugadores()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al actualizar jugador: ${throwable.message}") }
            }
        }
    }

    fun deleteJugador(id: Long) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = jugadorRepository.deleteJugador(id)
            result.onSuccess {
                loadJugadores()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar jugador: ${throwable.message}") }
            }
        }
    }
}
