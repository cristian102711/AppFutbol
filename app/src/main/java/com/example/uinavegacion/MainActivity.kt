package com.example.uinavegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.uinavegacion.data.network.RetrofitInstance
import com.example.uinavegacion.data.repository.EquipoRepository
import com.example.uinavegacion.data.repository.JugadorRepository
import com.example.uinavegacion.data.repository.PartidoRepository
import com.example.uinavegacion.navigation.NavGraph
import com.example.uinavegacion.ui.theme.UINavegacionTheme
import com.example.uinavegacion.ui.viewmodel.CreateMatchViewModel
import com.example.uinavegacion.ui.viewmodel.EquipoViewModel
import com.example.uinavegacion.ui.viewmodel.JugadorViewModel
import com.example.uinavegacion.ui.viewmodel.PartidoViewModel
import com.example.uinavegacion.ui.viewmodel.ViewModelFactory
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.CreateTeamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UINavegacionTheme {
                val navController = rememberNavController()

                // --- Construcción de Dependencias para la Red ---
                val apiService = RetrofitInstance.api
                val partidoRepository = PartidoRepository(apiService)
                val jugadorRepository = JugadorRepository(apiService)
                val equipoRepository = EquipoRepository(apiService)

                // La Factory ahora recibe todos los repositorios
                val viewModelFactory = ViewModelFactory(partidoRepository, jugadorRepository, equipoRepository)

                // Inicialización de ViewModels
                val authViewModel: AuthViewModel = viewModel()
                val createTeamViewModel: CreateTeamViewModel = viewModel()
                val partidoViewModel: PartidoViewModel = viewModel(factory = viewModelFactory)
                val jugadorViewModel: JugadorViewModel = viewModel(factory = viewModelFactory)
                val equipoViewModel: EquipoViewModel = viewModel(factory = viewModelFactory)
                val createMatchViewModel: CreateMatchViewModel = viewModel(factory = viewModelFactory)

                // Llamada al NavGraph con todos los ViewModels
                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    createTeamViewModel = createTeamViewModel,
                    partidoViewModel = partidoViewModel,
                    jugadorViewModel = jugadorViewModel,
                    equipoViewModel = equipoViewModel,
                    createMatchViewModel = createMatchViewModel
                )
            }
        }
    }
}
