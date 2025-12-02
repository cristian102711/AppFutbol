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

    // NOTA: Mantenemos 'nombreRival' en los argumentos para que tu UI no se rompa,
    // pero internamente usaremos un ID fijo temporalmente.
    fun createPartido(nombreRival: String, fecha: String, resultado: String) {
        _uiState.update { it.copy(isLoading = true, error = null, success = false) }

        viewModelScope.launch {
            // CORRECCIÓN: Adaptamos la creación del objeto al nuevo modelo Partido.
            // 1. rivalId = 1: Ponemos un ID fijo (1) porque el backend pide un número.
            //    (Más adelante tendrás que cambiar el campo de texto por un selector de equipos).
            // 2. Agregamos golesFavor y golesContra en 0 por defecto.
            val nuevoPartido = Partido(
                id = 0,
                fecha = fecha,
                rivalId = 1, // <--- TEMPORAL: Usamos el ID 1 para probar la conexión
                resultado = resultado,
                golesFavor = 0,
                golesContra = 0
            )

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