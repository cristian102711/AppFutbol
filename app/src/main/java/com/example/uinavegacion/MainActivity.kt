package com.example.uinavegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.uinavegacion.navigation.NavGraph
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.CreateTeamViewModel
// --- IMPORTANTE: Aquí está la corrección de la mayúscula ---
import com.example.uinavegacion.ui.theme.UINavegacionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // --- AQUÍ TAMBIÉN ESTÁ LA CORRECCIÓN (UINavegacionTheme) ---
            UINavegacionTheme {

                // 1. Creamos el controlador de navegación
                val navController = rememberNavController()

                // 2. Inicializamos los ViewModels
                val authViewModel: AuthViewModel = viewModel()
                val createTeamViewModel: CreateTeamViewModel = viewModel()

                // 3. Llamamos al NavGraph
                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    createTeamViewModel = createTeamViewModel
                )
            }
        }
    }
}