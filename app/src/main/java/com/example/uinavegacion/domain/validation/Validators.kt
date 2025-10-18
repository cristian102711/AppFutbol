package com.example.uinavegacion.domain.validation // Make sure the package matches

import android.util.Patterns // Needed for email validation


fun validateNameLettersOnly(nombre: String): String? {
    if (nombre.isBlank()) return "El nombre es obligatorio"
    // This regex allows letters (including accented ones) and spaces
    val regex = Regex("^[\\p{L} ]+$")
    return if (!regex.matches(nombre)) "Solo se aceptan letras y espacios" else null
}


fun validateEmail(email: String): String? {
    if (email.isBlank()) return "El correo es obligatorio"
    val ok = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    return if (!ok) "Formato de correo inválido" else null
}


fun validateStrongPass(pass: String): String? {
    if (pass.isBlank()) return "Debes escribir tu contraseña"
    if (pass.length < 8) return "Debe tener al menos 8 caracteres"
    if (!pass.any { it.isUpperCase() }) return "Debe contener al menos una mayúscula"
    if (!pass.any { it.isDigit() }) return "Debe contener al menos un número"
    // You could add more checks here (e.g., special characters) if needed
    return null // Password is valid
}

fun validateConfirm(pass: String, confirm: String): String? {
    if (confirm.isBlank()) return "Debes confirmar su contraseña"
    return if (pass != confirm) "Las contraseñas no coinciden" else null
}


fun validateLoginPassword(pass: String): String? {
    if (pass.isBlank()) return "La contraseña es obligatoria"
    return null
}