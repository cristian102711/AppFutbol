@file:OptIn(ExperimentalMaterial3Api::class) // Needed for Material 3 components

package com.example.uinavegacion.ui.screen // Make sure the package matches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.* // Needed for remember, mutableStateOf, collectAsState, LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController // Needed for navigation
import com.example.uinavegacion.navigation.Route // Import our defined routes
import com.example.uinavegacion.ui.theme.GrisComponente // Import custom colors
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.viewmodel.AuthViewModel // Import our ViewModel
import com.example.uinavegacion.viewmodel.LoginState // Import the LoginState sealed class

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    // 1. State for showing temporary messages (Snackbars)
    val snackbarHostState = remember { SnackbarHostState() }
    // 2. Observe the login state from the ViewModel
    val loginState by authViewModel.loginState.collectAsState()

    // 3. LaunchedEffect: Reacts to changes in loginState
    //    It runs *outside* the main UI composition. Perfect for side effects like navigation or showing Snackbars.
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                // Show success message
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                // Navigate to Home and clear back stack
                navController.navigate(Route.Home.path) {
                    popUpTo(0) // Clears all previous screens
                }
                authViewModel.resetLoginState() // Reset state in ViewModel
            }
            is LoginState.Error -> {
                // Show error message
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                authViewModel.resetLoginState() // Reset state in ViewModel
            }
            else -> {
                // Do nothing for Idle or Loading states here
            }
        }
    }

    // 4. Scaffold provides basic layout structure + slot for Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        // Box allows positioning elements like the "Register" link at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(16.dp)        // Apply our own padding
        ) {
            // Main content column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Make column scrollable if content overflows
                verticalArrangement = Arrangement.Center,    // Center content vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
            ) {
                Text(
                    text = "Bienvenido de nuevo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground // Use theme color
                )
                Text(
                    text = "Inicia sesión en tu cuenta",
                    fontSize = 16.sp,
                    color = TextoGris // Use custom gray color
                )
                Spacer(modifier = Modifier.height(48.dp)) // Space

                // Email TextField
                OutlinedTextField(
                    value = authViewModel.loginEmail, // Read value from ViewModel
                    onValueChange = { authViewModel.onLoginEmailChanged(it) }, // Notify ViewModel on change
                    label = { Text("Email") },
                    isError = authViewModel.loginEmailError != null, // Show error state from ViewModel
                    supportingText = { // Display error message from ViewModel if it exists
                        authViewModel.loginEmailError?.let { errorMsg -> Text(text = errorMsg) }
                    },
                    modifier = Modifier.fillMaxWidth(), // Take full width
                    shape = RoundedCornerShape(8.dp),   // Rounded corners
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Set keyboard type
                    singleLine = true,                  // Single line input
                    colors = TextFieldDefaults.colors( // Custom colors for dark theme
                        focusedIndicatorColor = VerdePrincipal, // Green border when focused
                        unfocusedIndicatorColor = Color.Transparent, // No border when unfocused
                        focusedContainerColor = GrisComponente, // Dark gray background when focused
                        unfocusedContainerColor = GrisComponente, // Dark gray background when unfocused
                        cursorColor = VerdePrincipal, // Green cursor
                        focusedLabelColor = TextoGris, // Gray label when focused
                        unfocusedLabelColor = TextoGris, // Gray label when unfocused
                        errorContainerColor = GrisComponente, // Background color in error state
                        errorIndicatorColor = MaterialTheme.colorScheme.error, // Red border in error state
                        errorLabelColor = MaterialTheme.colorScheme.error, // Red label in error state
                        errorSupportingTextColor = MaterialTheme.colorScheme.error // Red supporting text in error state
                    )
                )
                Spacer(modifier = Modifier.height(16.dp)) // Space

                // Password TextField
                OutlinedTextField(
                    value = authViewModel.loginPassword, // Read value from ViewModel
                    onValueChange = { authViewModel.onLoginPasswordChanged(it) }, // Notify ViewModel on change
                    label = { Text("Contraseña") },
                    isError = authViewModel.loginPasswordError != null, // Show error state from ViewModel
                    supportingText = { // Display error message from ViewModel if it exists
                        authViewModel.loginPasswordError?.let { errorMsg -> Text(text = errorMsg) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(), // Hide password characters
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Set keyboard type
                    singleLine = true,
                    colors = TextFieldDefaults.colors( // Apply the same custom colors
                        focusedIndicatorColor = VerdePrincipal,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = GrisComponente,
                        unfocusedContainerColor = GrisComponente,
                        cursorColor = VerdePrincipal,
                        focusedLabelColor = TextoGris,
                        unfocusedLabelColor = TextoGris,
                        errorContainerColor = GrisComponente,
                        errorIndicatorColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorSupportingTextColor = MaterialTheme.colorScheme.error
                    )
                )
                Spacer(modifier = Modifier.height(32.dp)) // Space

                // Login Button
                Button(
                    onClick = { authViewModel.loginUser() }, // Trigger login function in ViewModel
                    enabled = loginState !is LoginState.Loading, // Disable button while loading
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal, // Green background
                        contentColor = Color.Black        // Black text
                    )
                ) {
                    // Show loading indicator or text based on state
                    if (loginState is LoginState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Entrar", fontWeight = FontWeight.Bold)
                    }
                }
            } // End of Column

            // "Register" link positioned at the bottom center
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                color = VerdePrincipal, // Green text
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Position at bottom
                    .padding(bottom = 32.dp)       // Padding from bottom edge
                    .clickable { navController.navigate(Route.Register.path) } // Navigate on click
            )
        } // End of Box
    } // End of Scaffold
}