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
import com.example.uinavegacion.viewmodel.RegistrationState // Import the RegistrationState sealed class

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {

    // 1. State for showing temporary messages (Snackbars)
    val snackbarHostState = remember { SnackbarHostState() }
    // 2. Observe the registration state from the ViewModel
    val registrationState by authViewModel.registrationState.collectAsState()

    // 3. LaunchedEffect: Reacts to changes in registrationState
    LaunchedEffect(registrationState) {
        when (val state = registrationState) {
            is RegistrationState.Success -> {
                // Show success message
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                // Navigate back to Login and clear Register from back stack
                navController.navigate(Route.Login.path) {
                    popUpTo(Route.Register.path) { inclusive = true } // Go back to Login, remove Register
                }
                authViewModel.resetRegistrationState() // Reset state in ViewModel
            }
            is RegistrationState.Error -> {
                // Show error message
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                authViewModel.resetRegistrationState() // Reset state in ViewModel
            }
            else -> {
                // Do nothing for Idle or Loading states here
            }
        }
    }

    // 4. Scaffold provides structure + Snackbar slot
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        // Box allows positioning elements like the "Login" link at the bottom
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
                    .verticalScroll(rememberScrollState()), // Make column scrollable
                verticalArrangement = Arrangement.Center,    // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Regístrate para empezar",
                    fontSize = 16.sp,
                    color = TextoGris
                )
                Spacer(modifier = Modifier.height(48.dp)) // Space

                // Name TextField
                OutlinedTextField(
                    value = authViewModel.name,
                    onValueChange = { authViewModel.onNameChanged(it) },
                    label = { Text("Nombre") },
                    isError = authViewModel.nameError != null,
                    supportingText = { authViewModel.nameError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors( // Apply custom dark theme colors
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
                Spacer(modifier = Modifier.height(16.dp))

                // Email TextField
                OutlinedTextField(
                    value = authViewModel.registerEmail,
                    onValueChange = { authViewModel.onRegisterEmailChanged(it) },
                    label = { Text("Email") },
                    isError = authViewModel.registerEmailError != null,
                    supportingText = { authViewModel.registerEmailError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(/* Copy colors from Name field */)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Password TextField
                OutlinedTextField(
                    value = authViewModel.registerPassword,
                    onValueChange = { authViewModel.onRegisterPasswordChanged(it) },
                    label = { Text("Contraseña") },
                    isError = authViewModel.registerPasswordError != null,
                    supportingText = { authViewModel.registerPasswordError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(/* Copy colors from Name field */)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password TextField
                OutlinedTextField(
                    value = authViewModel.confirmPassword,
                    onValueChange = { authViewModel.onConfirmPasswordChanged(it) },
                    label = { Text("Confirmar Contraseña") },
                    isError = authViewModel.confirmPasswordError != null,
                    supportingText = { authViewModel.confirmPasswordError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(/* Copy colors from Name field */)
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Register Button
                Button(
                    onClick = { authViewModel.registerUser() }, // Trigger register function in ViewModel
                    enabled = registrationState !is RegistrationState.Loading, // Disable while loading
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.Black
                    )
                ) {
                    // Show loading indicator or text
                    if (registrationState is RegistrationState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Crear cuenta", fontWeight = FontWeight.Bold)
                    }
                }
            } // End of Column

            // "Login" link positioned at the bottom center
            Text(
                text = "¿Ya tienes una cuenta? Inicia Sesión",
                color = VerdePrincipal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Position at bottom
                    .padding(bottom = 32.dp)       // Padding from bottom
                    .clickable { navController.popBackStack() } // Go back to the previous screen (Login)
            )
        } // End of Box
    } // End of Scaffold
}