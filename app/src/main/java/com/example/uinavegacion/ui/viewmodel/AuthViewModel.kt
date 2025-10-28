package com.example.uinavegacion.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.domain.validation.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.net.Uri

// --- Data class para simular un usuario ---
// Ahora incluye el nombre
private data class User(val name: String, val email: String, val pass: String)

// --- Estados (RegistrationState y LoginState sin cambios) ---
sealed class RegistrationState { /* ... sin cambios ... */
    object Idle : RegistrationState(); object Loading : RegistrationState()
    data class Success(val message: String) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}
sealed class LoginState { /* ... sin cambios ... */
    object Idle : LoginState(); object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class AuthViewModel : ViewModel() {

    // --- AÑADE ESTA LÍNEA ---
    /** Guarda la URI (dirección) de la foto de perfil seleccionada. */
    var profileImageUri by mutableStateOf<Uri?>(null)
        private set

    // --- AÑADE ESTA FUNCIÓN ---
    fun onProfileImageSelected(uri: Uri?) {
        profileImageUri = uri
    }

    // --- Base de datos simulada ---
    private val _registeredUsers = mutableListOf<User>()

    // --- NUEVO: Estado para el nombre del usuario actual ---
    var currentUserName by mutableStateOf<String?>(null); private set

    // --- ESTADO para REGISTRO (sin cambios en variables, solo en registerUser) ---
    var name by mutableStateOf(""); private set
    var nameError by mutableStateOf<String?>(null); private set
    var registerEmail by mutableStateOf(""); private set
    var registerEmailError by mutableStateOf<String?>(null); private set
    var registerPassword by mutableStateOf(""); private set
    var registerPasswordError by mutableStateOf<String?>(null); private set
    var confirmPassword by mutableStateOf(""); private set
    var confirmPasswordError by mutableStateOf<String?>(null); private set
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()

    // --- ESTADO para LOGIN (sin cambios) ---
    var loginEmail by mutableStateOf(""); private set
    var loginEmailError by mutableStateOf<String?>(null); private set
    var loginPassword by mutableStateOf(""); private set
    var loginPasswordError by mutableStateOf<String?>(null); private set
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    // --- FUNCIONES onValueChanged (sin cambios) ---
    fun onNameChanged(n: String) { name = n; nameError = validateNameLettersOnly(n) }
    fun onRegisterEmailChanged(e: String) { registerEmail = e; registerEmailError = validateEmail(e) }
    fun onRegisterPasswordChanged(p: String) { /* ... sin cambios ... */
        registerPassword = p
        registerPasswordError = validateStrongPass(p)
        if (confirmPassword.isNotEmpty()) {
            confirmPasswordError = validateConfirm(p, confirmPassword)
        }
    }
    fun onConfirmPasswordChanged(c: String) { confirmPassword = c; confirmPasswordError = validateConfirm(registerPassword, c) }
    fun onLoginEmailChanged(e: String) { loginEmail = e; loginEmailError = null }
    fun onLoginPasswordChanged(p: String) { loginPassword = p; loginPasswordError = null }

    // --- LÓGICA PRINCIPAL ---

    fun registerUser() {
        if (!validateRegistrationForm()) {
            _registrationState.value = RegistrationState.Error("Por favor, corrige los errores.")
            return
        }
        if (_registeredUsers.any { it.email.equals(registerEmail, ignoreCase = true) }) {
            _registrationState.value = RegistrationState.Error("Este correo electrónico ya está en uso.")
            return
        }
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            delay(1500)
            // GUARDAMOS EL NOMBRE al registrar
            val newUser = User(name = name.trim(), email = registerEmail, pass = registerPassword)
            _registeredUsers.add(newUser)
            Log.d("AuthViewModel", "Usuario registrado: $newUser. Total: ${_registeredUsers.size}")
            clearRegistrationFields()
            _registrationState.value = RegistrationState.Success("¡Cuenta creada con éxito!")
        }
    }

    fun loginUser() {
        val emailErr = validateEmail(loginEmail); val passErr = validateLoginPassword(loginPassword)
        loginEmailError = emailErr; loginPasswordError = passErr
        if (emailErr != null || passErr != null) {
            _loginState.value = LoginState.Error("Por favor, completa los campos.")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1500)
            val user = _registeredUsers.find { it.email.equals(loginEmail, ignoreCase = true) }
            if (user != null && user.pass == loginPassword) {
                // GUARDAMOS EL NOMBRE DEL USUARIO al iniciar sesión
                currentUserName = user.name
                Log.d("AuthViewModel", "Inicio de sesión exitoso para: ${user.email}, Nombre: $currentUserName")
                _loginState.value = LoginState.Success("¡Inicio de sesión exitoso!")
            } else {
                currentUserName = null // Limpiamos el nombre si falla
                Log.d("AuthViewModel", "Fallo de inicio de sesión para: $loginEmail")
                _loginState.value = LoginState.Error("Usuario no registrado o contraseña incorrecta.")
            }
        }
    }

    // --- NUEVA FUNCIÓN: Login como Invitado ---
    /** Simula el inicio de sesión como invitado. */
    fun loginAsGuest() {
        // Establece el nombre de usuario como "Invitado"
        currentUserName = "Invitado"
        Log.d("AuthViewModel", "Inicio de sesión como invitado.")
        // (Opcional) Podrías emitir un LoginState.Success si quisieras mostrar un Snackbar
        // _loginState.value = LoginState.Success("Bienvenido Invitado")
    }

    // --- NUEVA FUNCIÓN: Logout ---
    /** Cierra la sesión del usuario actual. */
    fun logoutUser() {
        // Limpia el nombre del usuario actual
        currentUserName = null
        // Limpia los campos de login por si acaso
        clearLoginFields()
        // Resetea los estados de login/registro
        resetLoginState()
        resetRegistrationState()
        Log.d("AuthViewModel", "Usuario cerró sesión.")
    }


    // --- FUNCIONES AUXILIARES (sin cambios en reset, clearFields, validate) ---
    fun clearLoginFields() { /* ... sin cambios ... */
        loginEmail = ""
        loginPassword = ""
        loginEmailError = null
        loginPasswordError = null
    }
    fun resetLoginState() { _loginState.value = LoginState.Idle }
    fun resetRegistrationState() { _registrationState.value = RegistrationState.Idle }
    private fun clearRegistrationFields() { /* ... sin cambios ... */
        name = ""; nameError = null
        registerEmail = ""; registerEmailError = null
        registerPassword = ""; registerPasswordError = null
        confirmPassword = ""; confirmPasswordError = null
    }
    private fun validateRegistrationForm(): Boolean { /* ... sin cambios ... */
        onNameChanged(name)
        onRegisterEmailChanged(registerEmail)
        onRegisterPasswordChanged(registerPassword)
        onConfirmPasswordChanged(confirmPassword)
        return nameError == null &&
                registerEmailError == null &&
                registerPasswordError == null &&
                confirmPasswordError == null
    }
}