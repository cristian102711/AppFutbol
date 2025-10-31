// --- ¡ASEGÚRATE DE QUE EL PAQUETE SEA ESTE! ---
package com.example.uinavegacion.ui.screen

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal

/**
 * Función de ayuda compartida que define los colores personalizados
 * para los OutlinedTextField en toda la app.
 */
@Composable
fun customTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        // Colores de Material 3 (TextField)
        focusedContainerColor = GrisComponente,
        unfocusedContainerColor = GrisComponente,
        disabledContainerColor = GrisComponente,
        focusedBorderColor = VerdePrincipal,
        unfocusedBorderColor = GrisComponente,
        focusedLabelColor = VerdePrincipal,
        unfocusedLabelColor = TextoGris,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = VerdePrincipal
    )
}