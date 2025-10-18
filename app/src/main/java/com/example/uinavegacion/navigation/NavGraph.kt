package com.example.uinavegacion.navigation // Asegúrate que el paquete coincida

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uinavegacion.ui.screen.HomeScreen
import com.example.uinavegacion.ui.screen.LoginScreen
import com.example.uinavegacion.ui.screen.RegisterScreen
import com.example.uinavegacion.viewmodel.AuthViewModel

/**
 * Define el grafo de navegación de la aplicación.
 *
 * @param navController El controlador que maneja la navegación entre pantallas.
 * @param authViewModel El ViewModel compartido entre las pantallas de autenticación.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    // NavHost es el contenedor que muestra la pantalla actual
    NavHost(
        navController = navController,
        startDestination = Route.Login.path // La app siempre inicia en el Login
    ) {
        // Define la pantalla de Login
        composable(route = Route.Login.path) {
            // Llama al Composable de LoginScreen, pasándole el NavController y el ViewModel
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        // Define la pantalla de Registro
        composable(route = Route.Register.path) {
            // Llama al Composable de RegisterScreen
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }

        // Define la pantalla Home
        composable(route = Route.Home.path) {
            // Llama al Composable de HomeScreen
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }

        // Puedes añadir más pantallas aquí en el futuro
        // composable("profile") { ProfileScreen(...) }
    }
}