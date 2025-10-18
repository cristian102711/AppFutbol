package com.example.uinavegacion // Make sure the package matches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // <-- CORRECT import for by viewModels()
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.uinavegacion.navigation.NavGraph
import com.example.uinavegacion.ui.theme.UINavegacionTheme
import com.example.uinavegacion.viewmodel.AuthViewModel // <-- CORRECT import for AuthViewModel

class MainActivity : ComponentActivity() {

    // This should now work correctly after adding the dependency and fixing imports
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UINavegacionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
}