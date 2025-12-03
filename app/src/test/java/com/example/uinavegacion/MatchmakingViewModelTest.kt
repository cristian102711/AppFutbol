package com.example.uinavegacion

import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.repository.RivalRepository
import com.example.uinavegacion.ui.viewmodel.MatchState
import com.example.uinavegacion.ui.viewmodel.MatchmakingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MatchmakingViewModelTest {

    // 1. Simulamos el Hilo Principal (Main Thread) porque en los tests no existe
    private val testDispatcher = StandardTestDispatcher()

    // 2. MOCKEAMOS el Repositorio (Creamos uno falso)
    private val mockRepository = mock(RivalRepository::class.java)

    private lateinit var viewModel: MatchmakingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Configuramos el ambiente
        viewModel = MatchmakingViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Limpiar al terminar
    }

    @Test
    fun `startMatchmaking encuentra rival exitosamente`() = runTest(testDispatcher) {
        // Creamos una lista falsa de rivales
        val listaFalsa = listOf(
            Rival(1, "Equipo Test A"),
            Rival(2, "Equipo Test B")
        )
        // Le decimos al mock: "Cuando te pidan rivales, devuelve esta lista, y di que fue Éxito"
        whenever(mockRepository.getRivales()).thenReturn(Result.success(listaFalsa))

        // --- ACCIÓN (When) ---
        // Iniciamos la búsqueda
        viewModel.startMatchmaking()

        // Avanzamos el tiempo virtual (porque tu ViewModel tiene un delay de 3000ms)
        testScheduler.advanceUntilIdle()

        // --- VERIFICACIÓN (Then) ---
        // Verificamos que el estado final sea "Found" (Encontrado)
        val estadoFinal = viewModel.uiState.value

        // ¡La prueba de fuego!
        assertTrue(estadoFinal is MatchState.Found)

        // Verificamos que el rival encontrado esté dentro de nuestra lista falsa
        val rivalEncontrado = (estadoFinal as MatchState.Found).rival
        assertTrue(listaFalsa.contains(rivalEncontrado))

        println("¡TEST PASADO! Se encontró al rival: ${rivalEncontrado.nombre}")
    }
}