package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uinavegacion.data.repository.EquipoRepository
import com.example.uinavegacion.data.repository.JugadorRepository
import com.example.uinavegacion.data.repository.PartidoRepository

class ViewModelFactory(
    private val partidoRepository: PartidoRepository,
    private val jugadorRepository: JugadorRepository,
    private val equipoRepository: EquipoRepository
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
            modelClass.isAssignableFrom(CreateMatchViewModel::class.java) -> {
                CreateMatchViewModel(partidoRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
