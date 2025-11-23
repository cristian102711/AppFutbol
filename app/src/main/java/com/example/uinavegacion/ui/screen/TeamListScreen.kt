package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.EquipoUiState
import com.example.uinavegacion.ui.viewmodel.EquipoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamListScreen(equipoViewModel: EquipoViewModel) {
    val uiState by equipoViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos Disponibles") },
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
            TeamListContent(uiState = uiState)
        }
    }
}

@Composable
private fun TeamListContent(uiState: EquipoUiState) {
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
        uiState.equipos.isEmpty() -> {
            Text(
                text = "No se encontraron equipos.",
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
                items(uiState.equipos) { equipo ->
                    TeamCard(teamName = equipo.nombre)
                }
            }
        }
    }
}

@Composable
fun TeamCard(teamName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        ListItem(
            headlineContent = { Text(teamName, fontWeight = FontWeight.Bold, color = Color.White) },
            leadingContent = {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = "Icono de equipo",
                    tint = VerdePrincipal
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}
