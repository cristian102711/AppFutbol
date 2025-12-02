package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.repository.RivalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estados de la pantalla de búsqueda
sealed class MatchState {
    object Idle : MatchState() // Quieto
    object Searching : MatchState() // Buscando (Animación)
    data class Found(val rival: Rival) : MatchState() // Encontrado
    data class Error(val message: String) : MatchState() // Error
}

class MatchmakingViewModel(private val rivalRepository: RivalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MatchState>(MatchState.Idle)
    val uiState: StateFlow<MatchState> = _uiState.asStateFlow()

    // Variable para guardar el rival encontrado y pasarlo a la siguiente pantalla
    var rivalEncontrado: Rival? = null
        private set

    fun startMatchmaking() {
        viewModelScope.launch {
            // 1. Cambiamos estado a "Buscando"
            _uiState.value = MatchState.Searching

            // 2. Simulamos "Pensando/Buscando" por 3 segundos (para dar emoción)
            delay(3000)

            // 3. Buscamos los rivales reales del servidor
            val result = rivalRepository.getRivales()

            result.onSuccess { listaRivales ->
                if (listaRivales.isNotEmpty()) {
                    // 4. ¡MAGIA! Elegimos uno al azar
                    val randomRival = listaRivales.random()
                    rivalEncontrado = randomRival
                    _uiState.value = MatchState.Found(randomRival)
                } else {
                    _uiState.value = MatchState.Error("No hay equipos rivales disponibles en tu zona.")
                }
            }.onFailure {
                _uiState.value = MatchState.Error("Error de conexión al buscar rival.")
            }
        }
    }

    fun resetState() {
        _uiState.value = MatchState.Idle
        rivalEncontrado = null
    }
}