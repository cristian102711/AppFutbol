package com.example.uinavegacion.viewmodel // O el paquete donde tengas tu Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uinavegacion.viewmodel.AuthViewModel // <-- ¡EL IMPORT CLAVE!

/**
 * Factory para crear instancias de AuthViewModel.
 * En este caso simple (sin dependencias en el constructor), no es estrictamente necesario
 * si usas 'by viewModels()' de activity-ktx, pero lo arreglamos para que compile.
 */
class AuthViewModelFactory : ViewModelProvider.Factory {

    // La función 'create' es llamada por el sistema para obtener una instancia del ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se pide es AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Si lo es, crea y devuelve una nueva instancia de AuthViewModel
            // Asegúrate de que AuthViewModel() no requiera argumentos aquí
            @Suppress("UNCHECKED_CAST") // Suprime la advertencia de casteo, es seguro aquí
            return AuthViewModel() as T
        }
        // Si se pide crear un ViewModel desconocido, lanza una excepción
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}