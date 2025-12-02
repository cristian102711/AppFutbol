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
import com.example.uinavegacion.data.repository.RivalRepository
import com.example.uinavegacion.navigation.NavGraph
import com.example.uinavegacion.ui.theme.UINavegacionTheme
import com.example.uinavegacion.ui.viewmodel.CreateMatchViewModel
import com.example.uinavegacion.ui.viewmodel.EquipoViewModel
import com.example.uinavegacion.ui.viewmodel.JugadorViewModel
import com.example.uinavegacion.ui.viewmodel.MatchmakingViewModel // <--- IMPORTANTE
import com.example.uinavegacion.ui.viewmodel.PartidoViewModel
import com.example.uinavegacion.ui.viewmodel.RivalViewModel
import com.example.uinavegacion.ui.viewmodel.ViewModelFactory
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.CreateTeamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UINavegacionTheme {
                val navController = rememberNavController()

                // --- 1. REPOSITORIOS ---
                val partidoRepository = PartidoRepository(RetrofitInstance.apiPartidos)
                val equipoRepository = EquipoRepository(RetrofitInstance.apiEquipos)
                val jugadorRepository = JugadorRepository(RetrofitInstance.apiJugadores)
                val rivalRepository = RivalRepository(RetrofitInstance.apiRivales)

                // --- 2. FACTORY ---
                val viewModelFactory = ViewModelFactory(
                    partidoRepository,
                    jugadorRepository,
                    equipoRepository,
                    rivalRepository
                )

                // --- 3. VIEWMODELS ---
                val authViewModel: AuthViewModel = viewModel()
                val createTeamViewModel: CreateTeamViewModel = viewModel()

                val partidoViewModel: PartidoViewModel = viewModel(factory = viewModelFactory)
                val jugadorViewModel: JugadorViewModel = viewModel(factory = viewModelFactory)
                val equipoViewModel: EquipoViewModel = viewModel(factory = viewModelFactory)
                val createMatchViewModel: CreateMatchViewModel = viewModel(factory = viewModelFactory)
                val rivalViewModel: RivalViewModel = viewModel(factory = viewModelFactory)

                // ESTE FALTABA:
                val matchmakingViewModel: MatchmakingViewModel = viewModel(factory = viewModelFactory)

                // --- 4. NAVGRAPH ---
                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    createTeamViewModel = createTeamViewModel,
                    partidoViewModel = partidoViewModel,
                    jugadorViewModel = jugadorViewModel,
                    equipoViewModel = equipoViewModel,
                    createMatchViewModel = createMatchViewModel,
                    rivalViewModel = rivalViewModel,
                    matchmakingViewModel = matchmakingViewModel // <--- SE PASA AQUÍ
                )
            }
        }
    }
}