package com.example.uinavegacion.navigation

sealed class Route(val path: String) {
    // --- Autenticación ---
    object Login : Route("login_screen")
    object Register : Route("register_screen")

    // --- Pantallas Principales ---
    object Home : Route("home_screen")
    object CreateTeam : Route("create_team_screen")
    object PlayerList : Route("player_list_screen")
    object TeamList : Route("team_list_screen") // <-- AÑADIDO

    object Map : Route("map_screen")

    // --- Flujo de Emparejamiento
    object MatchmakingStart : Route("matchmaking_start_screen")
    object Matchmaking : Route("matchmaking_screen")
    object AvailableTeams : Route("available_teams_screen")

    object RivalList : Route("rival_list")

    object MatchFound : Route("match_found_screen")

    object Chat : Route("chat_screen")
    object Reservation : Route("reservation_screen")
    object CreateMatch : Route("create_match_screen")
    object CourtList : Route("court_list_screen")

    object Booking : Route(path = "booking_screen")

    object Stats : Route("stats_screen")

}