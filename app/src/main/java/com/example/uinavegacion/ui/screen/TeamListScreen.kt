package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.EquipoUiState
import com.example.uinavegacion.ui.viewmodel.EquipoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamListScreen(equipoViewModel: EquipoViewModel) {
    val uiState by equipoViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var editingEquipo by remember { mutableStateOf<Equipo?>(null) }
    var inputNombre by remember { mutableStateOf("") }
    var inputEntrenador by remember { mutableStateOf("") }
    var inputEscudo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos - CRUD") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                    editingEquipo = null
                    inputNombre = ""
                    inputEntrenador = ""
                    inputEscudo = ""
                },
                containerColor = VerdePrincipal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear equipo")
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
            TeamListContent(
                uiState = uiState,
                onEdit = { equipo ->
                    editingEquipo = equipo
                    inputNombre = equipo.nombre ?: ""
                    inputEntrenador = equipo.entrenador ?: ""
                    inputEscudo = equipo.escudoUrl ?: ""
                    showDialog = true
                },
                onDelete = { equipoViewModel.deleteEquipo(it) }
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; editingEquipo = null },
            title = {
                Text(
                    if (editingEquipo == null) "Crear Equipo" else "Editar Equipo",
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = inputNombre,
                        onValueChange = { inputNombre = it },
                        label = { Text("Nombre del equipo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedLabelColor = VerdePrincipal)
                    )
                    OutlinedTextField(
                        value = inputEntrenador,
                        onValueChange = { inputEntrenador = it },
                        label = { Text("Entrenador") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedLabelColor = VerdePrincipal)
                    )
                    OutlinedTextField(
                        value = inputEscudo,
                        onValueChange = { inputEscudo = it },
                        label = { Text("URL del escudo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedLabelColor = VerdePrincipal)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputNombre.isNotBlank()) {
                            val equipo = Equipo(
                                id = editingEquipo?.id ?: 0,
                                nombre = inputNombre,
                                entrenador = inputEntrenador.ifBlank { null },
                                escudoUrl = inputEscudo.ifBlank { null }
                            )
                            if (editingEquipo == null) {
                                equipoViewModel.createEquipo(equipo)
                            } else {
                                equipoViewModel.updateEquipo(equipo)
                            }
                            showDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false; editingEquipo = null }) {
                    Text("Cancelar")
                }
            },
            containerColor = GrisComponente
        )
    }
}

@Composable
private fun TeamListContent(
    uiState: EquipoUiState,
    onEdit: (Equipo) -> Unit,
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
        uiState.equipos.isEmpty() -> {
            Text(text = "No se encontraron equipos.", color = TextoGris)
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.equipos) { equipo ->
                    TeamCardWithActions(
                        equipo = equipo,
                        onEdit = { onEdit(equipo) },
                        onDelete = { onDelete(equipo.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamCardWithActions(equipo: Equipo, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = "Equipo",
                    tint = VerdePrincipal,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text(equipo.nombre ?: "Equipo desconocido", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Entrenador: ${equipo.entrenador ?: "N/A"}", color = TextoGris)
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
    }
}