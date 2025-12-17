package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.data.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para representar el estado de la UI de la lista de equipos
data class EquipoUiState(
    val equipos: List<Equipo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class EquipoViewModel(private val equipoRepository: EquipoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipoUiState())
    val uiState: StateFlow<EquipoUiState> = _uiState.asStateFlow()

    init {
        loadEquipos()
    }

    fun loadEquipos() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = equipoRepository.getEquipos()
            result.onSuccess { equipos ->
                _uiState.update {
                    it.copy(isLoading = false, equipos = equipos)
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar equipos: ${throwable.message}")
                }
            }
        }
    }

    // --- Nuevas acciones CRUD ---

    fun createEquipo(equipo: Equipo) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = equipoRepository.createEquipo(equipo)
            result.onSuccess {
                loadEquipos()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al crear equipo: ${throwable.message}") }
            }
        }
    }

    fun updateEquipo(equipo: Equipo) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = equipoRepository.updateEquipo(equipo)
            result.onSuccess {
                loadEquipos()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al actualizar equipo: ${throwable.message}") }
            }
        }
    }

    fun deleteEquipo(id: Long) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = equipoRepository.deleteEquipo(id)
            result.onSuccess {
                loadEquipos()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar equipo: ${throwable.message}") }
            }
        }
    }
}
