package com.example.uinavegacion.ui.theme // Asegúrate que el paquete coincida

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Definimos la paleta SOLO para el modo oscuro usando nuestros colores
private val DarkColorScheme = darkColorScheme(
    primary = VerdePrincipal,       // Color principal (botones, acentos)
    background = NegroFondo,        // Fondo principal de las pantallas
    surface = GrisComponente,       // Fondo de elementos sobre el background (cards, textfields)
    onPrimary = NegroFondo,         // Color del texto/iconos sobre el color primario
    onBackground = TextoBlanco,     // Color del texto/iconos sobre el fondo
    onSurface = TextoBlanco,        // Color del texto/iconos sobre superficies
    error = RojoError               // Color para indicar errores
    // Puedes definir otros colores si los necesitas (secondary, tertiary, etc.)
)

@Composable
fun UINavegacionTheme( // Puedes cambiar "UINavegacionTheme" al nombre de tu app si quieres
    // Forzamos el tema oscuro, ignorando el del sistema
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Siempre usamos nuestra paleta oscura personalizada
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Hacemos que la barra de estado superior sea negra también
            window.statusBarColor = colorScheme.background.toArgb()
            // Aseguramos que los iconos de la barra de estado (hora, batería) sean claros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Usará la tipografía definida en Type.kt
        content = content
    )
}