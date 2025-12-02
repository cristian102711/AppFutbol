package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag // Icono de bandera para rivales
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
import com.example.uinavegacion.ui.viewmodel.RivalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RivalListScreen(rivalViewModel: RivalViewModel) {
    val uiState by rivalViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rivales Históricos") },
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
            when {
                uiState.isLoading -> CircularProgressIndicator(color = VerdePrincipal)
                uiState.error != null -> Text(text = uiState.error!!, color = Color.Red)
                uiState.rivals.isEmpty() -> Text("No hay rivales registrados", color = TextoGris)
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.rivals) { rival ->
                            RivalCard(name = rival.nombre ?: "Rival desconocido")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RivalCard(name: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        ListItem(
            headlineContent = { Text(name, color = Color.White, fontWeight = FontWeight.Bold) },
            leadingContent = { Icon(Icons.Default.Flag, contentDescription = null, tint = VerdePrincipal) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}