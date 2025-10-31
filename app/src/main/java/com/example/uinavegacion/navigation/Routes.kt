package com.example.uinavegacion.navigation

sealed class Route(val path: String) {
    // --- Autenticación ---
    object Login : Route("login_screen")
    object Register : Route("register_screen")

    // --- Pantallas Principales ---
    object Home : Route("home_screen")
    object CreateTeam : Route("create_team_screen")
    object Map : Route("map_screen")
    object Stats : Route("stats_screen")

    // --- Flujo de Emparejamiento
    object MatchmakingStart : Route("matchmaking_start_screen")
    object Matchmaking : Route("matchmaking_screen")
    object AvailableTeams : Route("available_teams_screen")

    object MatchFound : Route("match_found_screen")     // <-- Esta es la que sigue
}