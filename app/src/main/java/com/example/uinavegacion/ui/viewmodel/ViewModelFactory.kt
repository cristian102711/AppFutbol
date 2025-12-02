package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uinavegacion.data.repository.EquipoRepository
import com.example.uinavegacion.data.repository.JugadorRepository
import com.example.uinavegacion.data.repository.PartidoRepository
import com.example.uinavegacion.data.repository.RivalRepository

class ViewModelFactory(
    private val partidoRepository: PartidoRepository,
    private val jugadorRepository: JugadorRepository,
    private val equipoRepository: EquipoRepository,
    private val rivalRepository: RivalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PartidoViewModel::class.java) -> {
                PartidoViewModel(partidoRepository) as T
            }
            modelClass.isAssignableFrom(JugadorViewModel::class.java) -> {
                JugadorViewModel(jugadorRepository) as T
            }
            modelClass.isAssignableFrom(EquipoViewModel::class.java) -> {
                EquipoViewModel(equipoRepository) as T
            }
            // --- AGREGADO: Lógica para crear RivalViewModel ---
            modelClass.isAssignableFrom(RivalViewModel::class.java) -> {
                RivalViewModel(rivalRepository) as T
            }
            modelClass.isAssignableFrom(CreateMatchViewModel::class.java) -> {
                CreateMatchViewModel(partidoRepository) as T
            }

            modelClass.isAssignableFrom(MatchmakingViewModel::class.java) -> {
                MatchmakingViewModel(rivalRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}