package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.repository.PartidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para representar el estado de la UI
data class PartidoUiState(
    val partidos: List<Partido> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PartidoViewModel(private val partidoRepository: PartidoRepository) : ViewModel() {

    // StateFlow privado y mutable que solo el ViewModel puede modificar
    private val _uiState = MutableStateFlow(PartidoUiState())
    // StateFlow público de solo lectura que la UI puede observar
    val uiState: StateFlow<PartidoUiState> = _uiState.asStateFlow()

    // El bloque init se ejecuta tan pronto como se crea el ViewModel
    init {
        loadPartidos()
    }

    // Función para cargar los partidos desde el repositorio
    fun loadPartidos() {
        // Actualiza el estado para mostrar que la carga ha comenzado
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = partidoRepository.getPartidos()
            result.onSuccess { partidos ->
                // En caso de éxito, actualiza el estado con la lista de partidos
                _uiState.update {
                    it.copy(isLoading = false, partidos = partidos)
                }
            }.onFailure { throwable ->
                // En caso de fallo, actualiza el estado con el mensaje de error
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar partidos: ${throwable.message}")
                }
            }
        }
    }

    // --- Nuevas acciones CRUD expuestas por el ViewModel ---

    fun createPartido(partido: Partido) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = partidoRepository.createPartido(partido)
            result.onSuccess {
                // Refrescar lista en caso de éxito
                loadPartidos()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al crear partido: ${throwable.message}") }
            }
        }
    }

    fun updatePartido(partido: Partido) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = partidoRepository.updatePartido(partido)
            result.onSuccess {
                loadPartidos()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al actualizar partido: ${throwable.message}") }
            }
        }
    }

    fun deletePartido(id: Long) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = partidoRepository.deletePartido(id)
            result.onSuccess {
                loadPartidos()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar partido: ${throwable.message}") }
            }
        }
    }
}
