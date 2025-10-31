package com.example.uinavegacion.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// --- Imports de las pantallas que SÍ existen ---
import com.example.uinavegacion.ui.screen.HomeScreen
import com.example.uinavegacion.ui.screen.LoginScreen
import com.example.uinavegacion.ui.screen.RegisterScreen
import com.example.uinavegacion.ui.screen.CreateTeamScreen
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.CreateTeamViewModel
import com.example.uinavegacion.ui.screen.MatchmakingScreen
import com.example.uinavegacion.ui.screen.MatchmakingStartScreen
import com.example.uinavegacion.ui.screen.AvailableTeamsScreen
import com.example.uinavegacion.ui.screen.MatchFoundScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        // --- Pantallas de Autenticación (funcionan) ---
        composable(route = Route.Login.path) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = Route.Register.path) {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = Route.Home.path) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Route.MatchmakingStart.path) {
            MatchmakingStartScreen(navController = navController)
        }
        composable(Route.Matchmaking.path) {
            MatchmakingScreen(navController = navController)
        }
        composable(Route.AvailableTeams.path) {
            AvailableTeamsScreen(navController = navController)
        }
        composable(Route.MatchFound.path) {
            MatchFoundScreen(navController = navController)
        }


        // --- Hemos borrado los composables rotos de CreateMatch y MyMatches ---

        // --- Pantalla de Crear Equipo (nueva) ---
        composable(route = Route.CreateTeam.path) {
            val createTeamViewModel: CreateTeamViewModel = viewModel()
            CreateTeamScreen( // <-- Este dará error
                navController = navController,
                viewModel = createTeamViewModel
            )
        }
    }
}