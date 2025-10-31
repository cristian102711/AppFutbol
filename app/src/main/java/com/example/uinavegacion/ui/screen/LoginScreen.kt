@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

// --- IMPORTS AÑADIDOS ---
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.uinavegacion.R
// ------------------------

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.viewmodel.AuthViewModel
import com.example.uinavegacion.viewmodel.LoginState


@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val loginState by authViewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                navController.navigate(Route.Home.path) {
                    popUpTo(0)
                }
                authViewModel.resetLoginState()
            }
            is LoginState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                authViewModel.resetLoginState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- INICIO DE LA ACTUALIZACIÓN ---

                // 1. Añadimos el nuevo logo aquí
                Image(
                    // Asegúrate de que tu logo se llame 'ic_app_logo.png' en 'res/drawable'
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "Logo de Soccer Match",
                    modifier = Modifier
                        .width(180.dp) // Le damos un buen tamaño
                        .padding(bottom = 16.dp), // Espacio entre el logo y el texto
                    contentScale = ContentScale.Fit // Esto evita que se recorte
                )

                // 2. Mantenemos tus textos de bienvenida (con el nombre actualizado)
                Text(
                    text = "Bienvenido a Next Match",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Inicia sesión en tu cuenta",
                    fontSize = 16.sp,
                    color = TextoGris
                )
                Spacer(modifier = Modifier.height(48.dp))

                // --- FIN DE LA ACTUALIZACIÓN ---


                // Email TextField (sin cambios)
                OutlinedTextField(
                    value = authViewModel.loginEmail,
                    onValueChange = { authViewModel.onLoginEmailChanged(it) },
                    label = { Text("Email") },
                    isError = authViewModel.loginEmailError != null,
                    supportingText = {
                        authViewModel.loginEmailError?.let { errorMsg -> Text(text = errorMsg) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
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

                // Password TextField (sin cambios)
                OutlinedTextField(
                    value = authViewModel.loginPassword,
                    onValueChange = { authViewModel.onLoginPasswordChanged(it) },
                    label = { Text("Contraseña") },
                    isError = authViewModel.loginPasswordError != null,
                    supportingText = {
                        authViewModel.loginPasswordError?.let { errorMsg -> Text(text = errorMsg) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
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
                Spacer(modifier = Modifier.height(32.dp))

                // Login Button (sin cambios)
                Button(
                    onClick = { authViewModel.loginUser() },
                    enabled = loginState !is LoginState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.Black
                    )
                ) {
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

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para Ingresar como invitado (sin cambios)
                Button(
                    onClick = {
                        authViewModel.loginAsGuest()
                        navController.navigate(Route.Home.path) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Ingresar como invitado", fontWeight = FontWeight.Bold)
                }

            } // Fin de la Column

            // "Register" link (sin cambios)
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                color = VerdePrincipal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clickable { navController.navigate(Route.Register.path) }
            )
        } // Fin del Box
    } // Fin del Scaffold
}