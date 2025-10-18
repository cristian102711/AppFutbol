package com.example.uinavegacion.navigation // Make sure the package matches

/**
 * Defines the unique path strings for each screen in the app.
 * Using this sealed class avoids typos when navigating.
 */
sealed class Route(val path: String) {
    object Login : Route("login")       // Path for the Login screen
    object Register : Route("register") // Path for the Register screen
    object Home : Route("home")         // Path for the Home screen
}