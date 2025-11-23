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

// Estado para la creación de un partido
data class CreateMatchUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class CreateMatchViewModel(private val partidoRepository: PartidoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateMatchUiState())
    val uiState: StateFlow<CreateMatchUiState> = _uiState.asStateFlow()

    fun createPartido(nombreRival: String, fecha: String, resultado: String) {
        _uiState.update { it.copy(isLoading = true, error = null, success = false) }

        viewModelScope.launch {
            // Creamos un objeto Partido. El ID lo pone el backend, así que usamos 0 como placeholder.
            val nuevoPartido = Partido(id = 0, nombreRival = nombreRival, fecha = fecha, resultado = resultado)
            
            val result = partidoRepository.createPartido(nuevoPartido)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, success = true) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = "Error: ${throwable.message}") }
            }
        }
    }

    // Función para resetear el estado una vez que la UI ha reaccionado
    fun resetState() {
        _uiState.value = CreateMatchUiState()
    }
}
