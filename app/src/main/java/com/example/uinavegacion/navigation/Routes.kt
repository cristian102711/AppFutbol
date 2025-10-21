package com.example.uinavegacion.navigation

sealed class Route(val path: String) {
    object Login : Route("login")
    object Register : Route("register")
    object Home : Route("home")
    object CreateTeam : Route("create_team")
}