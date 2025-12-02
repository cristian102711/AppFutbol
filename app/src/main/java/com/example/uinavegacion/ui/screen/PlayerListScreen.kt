package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.JugadorUiState
import com.example.uinavegacion.ui.viewmodel.JugadorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListScreen(jugadorViewModel: JugadorViewModel) {
    val uiState by jugadorViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Jugadores") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            PlayerListContent(uiState = uiState)
        }
    }
}

@Composable
private fun PlayerListContent(uiState: JugadorUiState) {
    when {
        uiState.isLoading -> {
            CircularProgressIndicator(color = VerdePrincipal)
        }
        uiState.error != null -> {
            Text(
                text = uiState.error,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        uiState.jugadores.isEmpty() -> {
            Text(
                text = "No se encontraron jugadores.",
                color = TextoGris,
                textAlign = TextAlign.Center
            )
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.jugadores) { jugador ->
                    // CORRECCIÓN: Usamos ?: para evitar errores nulos
                    // Y pasamos el 'dorsal' en lugar del 'nivel'
                    PlayerCard(
                        name = jugador.nombre ?: "Nombre desconocido",
                        position = jugador.posicion ?: "Posición n/a",
                        dorsal = jugador.dorsal ?: 0 // Si no tiene número, ponemos 0
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerCard(name: String, position: String, dorsal: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Posición: $position", fontSize = 16.sp, color = TextoGris)
            }

            // CORRECCIÓN VISUAL:
            // En lugar de una barra de progreso de "nivel" (que no tenemos),
            // mostramos el DORSAL (Número de camiseta) en un círculo.
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(VerdePrincipal.copy(alpha = 0.2f)) // Fondo suave
            ) {
                Text(
                    text = if (dorsal > 0) "#$dorsal" else "-",
                    color = VerdePrincipal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}