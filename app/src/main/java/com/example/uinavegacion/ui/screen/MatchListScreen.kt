package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.PartidoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchListScreen(partidoViewModel: PartidoViewModel) {
    val uiState by partidoViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var editingPartido by remember { mutableStateOf<Partido?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Partidos - CRUD") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingPartido = null // Para asegurar que es modo creación
                    showDialog = true
                },
                containerColor = VerdePrincipal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear partido")
            }
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            MatchListContent(
                uiState = uiState,
                onEdit = { partido ->
                    editingPartido = partido
                    showDialog = true
                },
                onDelete = { partidoViewModel.deletePartido(it) }
            )
        }
    }

    if (showDialog) {
        PartidoFormDialog(
            partido = editingPartido,
            onDismiss = { showDialog = false },
            onConfirm = {
                if (it.id == 0L) {
                    partidoViewModel.createPartido(it)
                } else {
                    partidoViewModel.updatePartido(it)
                }
                showDialog = false
            }
        )
    }
}

@Composable
private fun MatchListContent(
    uiState: com.example.uinavegacion.ui.viewmodel.PartidoUiState,
    onEdit: (Partido) -> Unit,
    onDelete: (Long) -> Unit
) {
    when {
        uiState.isLoading -> CircularProgressIndicator(color = VerdePrincipal)
        uiState.error != null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "❌ ${uiState.error}", color = Color.Red)
            }
        }
        uiState.partidos.isEmpty() -> {
            Text(text = "No hay partidos registrados.", color = TextoGris)
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.partidos) { partido ->
                    MatchCardWithActions(
                        partido = partido,
                        onEdit = { onEdit(partido) },
                        onDelete = { onDelete(partido.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MatchCardWithActions(partido: Partido, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.SportsSoccer,
                        contentDescription = "Partido",
                        tint = VerdePrincipal,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = "Vs Rival #${partido.rivalId ?: "?"}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${partido.golesFavor ?: 0} - ${partido.golesContra ?: 0}",
                            color = VerdePrincipal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = VerdePrincipal)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Fecha: ${partido.fecha ?: "N/A"}",
                    color = TextoGris,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Resultado: ${partido.resultado ?: "Pendiente"}",
                    color = TextoGris,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidoFormDialog(
    partido: Partido?, // Si es null, es para crear. Si no, para editar.
    onDismiss: () -> Unit,
    onConfirm: (Partido) -> Unit
) {
    var inputFecha by remember { mutableStateOf(partido?.fecha ?: "") }
    var inputResultado by remember { mutableStateOf(partido?.resultado ?: "") }
    var inputGolesFavor by remember { mutableStateOf(partido?.golesFavor?.toString() ?: "") }
    var inputGolesContra by remember { mutableStateOf(partido?.golesContra?.toString() ?: "") }
    var inputRivalId by remember { mutableStateOf(partido?.rivalId?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (partido == null) "Crear Partido" else "Editar Partido",
                color = Color.White
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = inputFecha,
                    onValueChange = { inputFecha = it },
                    label = { Text("Fecha (ej: 2024-12-16)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedLabelColor = VerdePrincipal
                    )
                )
                OutlinedTextField(
                    value = inputResultado,
                    onValueChange = { inputResultado = it },
                    label = { Text("Resultado (ej: GANADO)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedLabelColor = VerdePrincipal
                    )
                )
                OutlinedTextField(
                    value = inputGolesFavor,
                    onValueChange = { inputGolesFavor = it },
                    label = { Text("Goles a favor") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedLabelColor = VerdePrincipal
                    )
                )
                OutlinedTextField(
                    value = inputGolesContra,
                    onValueChange = { inputGolesContra = it },
                    label = { Text("Goles en contra") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedLabelColor = VerdePrincipal
                    )
                )
                OutlinedTextField(
                    value = inputRivalId,
                    onValueChange = { inputRivalId = it },
                    label = { Text("ID del Rival") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedLabelColor = VerdePrincipal
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (inputFecha.isNotBlank()) {
                        val partidoConfirmado = Partido(
                            id = partido?.id ?: 0,
                            fecha = inputFecha,
                            resultado = inputResultado.ifBlank { null },
                            golesFavor = inputGolesFavor.toIntOrNull(),
                            golesContra = inputGolesContra.toIntOrNull(),
                            rivalId = inputRivalId.toLongOrNull()
                        )
                        onConfirm(partidoConfirmado)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = GrisComponente
    )
}

