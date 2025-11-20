package com.example.uinavegacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text

// --- Imports de Pantallas ---
import com.example.uinavegacion.ui.screen.AvailableTeamsScreen
import com.example.uinavegacion.ui.screen.BookingScreen
import com.example.uinavegacion.ui.screen.ChatScreen
import com.example.uinavegacion.ui.screen.CourtListScreen
import com.example.uinavegacion.ui.screen.CreateMatchScreen
import com.example.uinavegacion.ui.screen.CreateTeamScreen
import com.example.uinavegacion.ui.screen.HomeScreen
import com.example.uinavegacion.ui.screen.LoginScreen
import com.example.uinavegacion.ui.screen.MatchFoundScreen
import com.example.uinavegacion.ui.screen.MatchmakingScreen
import com.example.uinavegacion.ui.screen.MatchmakingStartScreen
import com.example.uinavegacion.ui.screen.RegisterScreen
import com.example.uinavegacion.ui.screen.ReservationScreen
import com.example.uinavegacion.ui.screen.MapScreen
import com.example.uinavegacion.ui.screen.StatsScreen

import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.CreateTeamViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    createTeamViewModel: CreateTeamViewModel
) {
    NavHost(navController = navController, startDestination = Route.Login.path) {

        // --- Perfil (Estadísticas) ---
        // Esta es la ÚNICA vez que debe aparecer esta ruta
        composable(Route.Stats.path) {
            StatsScreen(navController = navController)
        }

        // --- Autenticación ---
        composable(Route.Login.path) { LoginScreen(navController, authViewModel) }
        composable(Route.Register.path) { RegisterScreen(navController, authViewModel) }

        // --- Home ---
        composable(Route.Home.path) { HomeScreen(navController, authViewModel) }

        // --- Canchas y Reservas ---
        composable(Route.Booking.path) { BookingScreen(navController) }
        composable(Route.CourtList.path) { CourtListScreen(navController) }

        // --- Mapa ---
        composable(Route.Map.path) { MapScreen(navController = navController) }

        // --- Emparejamiento ---
        composable(Route.MatchmakingStart.path) { MatchmakingStartScreen(navController) }
        composable(Route.Matchmaking.path) { MatchmakingScreen(navController) }
        composable(Route.AvailableTeams.path) { AvailableTeamsScreen(navController) }
        composable(Route.MatchFound.path) { MatchFoundScreen(navController) }

        // --- Crear Equipo ---
        composable(Route.CreateTeam.path) {
            CreateTeamScreen(navController = navController, viewModel = createTeamViewModel)
        }

        // --- Otros ---
        composable(Route.Chat.path) { ChatScreen(navController) }
        composable(Route.Reservation.path) { ReservationScreen(navController) }
        composable(Route.CreateMatch.path) { CreateMatchScreen(navController) }
    }
}