package com.example.uinavegacion.viewmodel // Make sure the package matches

import android.util.Log // For printing logs during simulation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Import the validation functions we created
import com.example.uinavegacion.domain.validation.*
import kotlinx.coroutines.delay // To simulate network calls
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch // For running background tasks

// --- Data class to simulate a user in our "database" ---
private data class User(val email: String, val pass: String)

// --- States to communicate registration progress/results to the UI ---
sealed class RegistrationState {
    object Idle : RegistrationState()      // Initial state
    object Loading : RegistrationState()   // Registration in progress
    data class Success(val message: String) : RegistrationState() // Success with a message
    data class Error(val message: String) : RegistrationState()   // Failure with a message
}

// --- States to communicate login progress/results to the UI ---
sealed class LoginState {
    object Idle : LoginState()         // Initial state
    object Loading : LoginState()      // Login in progress
    data class Success(val message: String) : LoginState() // Success with a message
    data class Error(val message: String) : LoginState()   // Failure with a message
}


// --- The ViewModel itself ---
class AuthViewModel : ViewModel() {

    // --- Simulated User Database (in-memory list) ---
    private val _registeredUsers = mutableListOf<User>()

    // --- STATE for REGISTER screen ---
    var name by mutableStateOf(""); private set
    var nameError by mutableStateOf<String?>(null); private set

    var registerEmail by mutableStateOf(""); private set
    var registerEmailError by mutableStateOf<String?>(null); private set

    var registerPassword by mutableStateOf(""); private set
    var registerPasswordError by mutableStateOf<String?>(null); private set

    var confirmPassword by mutableStateOf(""); private set
    var confirmPasswordError by mutableStateOf<String?>(null); private set

    // StateFlow to report registration status back to the UI
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()


    // --- STATE for LOGIN screen ---
    var loginEmail by mutableStateOf(""); private set
    var loginEmailError by mutableStateOf<String?>(null); private set

    var loginPassword by mutableStateOf(""); private set
    var loginPasswordError by mutableStateOf<String?>(null); private set

    // StateFlow to report login status back to the UI
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()


    // --- FUNCTIONS called by the UI when user types ---
    fun onNameChanged(n: String) { name = n; nameError = validateNameLettersOnly(n) }
    fun onRegisterEmailChanged(e: String) { registerEmail = e; registerEmailError = validateEmail(e) }
    fun onRegisterPasswordChanged(p: String) {
        registerPassword = p
        registerPasswordError = validateStrongPass(p)
        // Re-validate confirmation if it was already typed
        if (confirmPassword.isNotEmpty()) {
            confirmPasswordError = validateConfirm(p, confirmPassword)
        }
    }
    fun onConfirmPasswordChanged(c: String) { confirmPassword = c; confirmPasswordError = validateConfirm(registerPassword, c) }
    fun onLoginEmailChanged(e: String) { loginEmail = e; loginEmailError = null /* Clear error on type */ }
    fun onLoginPasswordChanged(p: String) { loginPassword = p; loginPasswordError = null /* Clear error on type */ }


    // --- CORE LOGIC FUNCTIONS ---

    /** Initiates the user registration process. */
    fun registerUser() {
        // 1. Run final validation on all fields
        if (!validateRegistrationForm()) {
            _registrationState.value = RegistrationState.Error("Por favor, corrige los errores.")
            return // Stop if validation fails
        }
        // 2. Check if email is already taken (case-insensitive)
        if (_registeredUsers.any { it.email.equals(registerEmail, ignoreCase = true) }) {
            _registrationState.value = RegistrationState.Error("Este correo electrónico ya está en uso.")
            return // Stop if email exists
        }

        // 3. Start background task for registration
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading // Inform UI we are loading
            delay(1500) // Simulate network delay

            // 4. SIMULATE SUCCESS: Add user to our list
            val newUser = User(email = registerEmail, pass = registerPassword)
            _registeredUsers.add(newUser)
            Log.d("AuthViewModel", "Usuario registrado: $newUser. Total: ${_registeredUsers.size}") // Log for debugging

            // 5. Clean up and report success
            clearRegistrationFields()
            _registrationState.value = RegistrationState.Success("¡Cuenta creada con éxito!")
        }
    }

    /** Initiates the user login process. */
    fun loginUser() {
        // 1. Validate input fields
        val emailErr = validateEmail(loginEmail)
        val passErr = validateLoginPassword(loginPassword)
        loginEmailError = emailErr // Update errors for UI
        loginPasswordError = passErr

        // 2. Stop if basic validation fails
        if (emailErr != null || passErr != null) {
            _loginState.value = LoginState.Error("Por favor, completa los campos.")
            return
        }

        // 3. Start background task for login
        viewModelScope.launch {
            _loginState.value = LoginState.Loading // Inform UI we are loading
            delay(1500) // Simulate network delay

            // 4. SIMULATE AUTHENTICATION: Check user exists and password matches
            val user = _registeredUsers.find { it.email.equals(loginEmail, ignoreCase = true) }

            if (user != null && user.pass == loginPassword) {
                // SUCCESS
                Log.d("AuthViewModel", "Inicio de sesión exitoso para: ${user.email}")
                _loginState.value = LoginState.Success("¡Inicio de sesión exitoso!")
            } else {
                // FAILURE
                Log.d("AuthViewModel", "Fallo de inicio de sesión para: $loginEmail")
                _loginState.value = LoginState.Error("Usuario no registrado o contraseña incorrecta.")
            }
        }
    }


    // --- HELPER FUNCTIONS ---

    /** Clears the login input fields and errors. */
    fun clearLoginFields() {
        loginEmail = ""
        loginPassword = ""
        loginEmailError = null
        loginPasswordError = null
    }

    /** Resets the login state to Idle (used after showing a message). */
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    /** Resets the registration state to Idle (used after showing a message). */
    fun resetRegistrationState() {
        _registrationState.value = RegistrationState.Idle
    }

    /** Clears all registration input fields and errors. */
    private fun clearRegistrationFields() {
        name = ""; nameError = null
        registerEmail = ""; registerEmailError = null
        registerPassword = ""; registerPasswordError = null
        confirmPassword = ""; confirmPasswordError = null
    }

    /** Runs validation on all registration fields and updates error states. Returns true if valid. */
    private fun validateRegistrationForm(): Boolean {
        // Trigger validation (and update error states) for all fields
        onNameChanged(name)
        onRegisterEmailChanged(registerEmail)
        onRegisterPasswordChanged(registerPassword)
        onConfirmPasswordChanged(confirmPassword)
        // Return true only if ALL error states are null
        return nameError == null &&
                registerEmailError == null &&
                registerPasswordError == null &&
                confirmPasswordError == null
    }
}