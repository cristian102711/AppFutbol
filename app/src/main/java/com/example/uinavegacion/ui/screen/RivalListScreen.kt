package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.RivalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RivalListScreen(rivalViewModel: RivalViewModel) {
    val uiState by rivalViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var editingRival by remember { mutableStateOf<Rival?>(null) }
    var inputNombre by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rivales - CRUD") },
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
                    editingRival = null
                    inputNombre = ""
                },
                containerColor = VerdePrincipal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear rival")
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
            when {
                uiState.isLoading -> CircularProgressIndicator(color = VerdePrincipal)
                uiState.error != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "❌ ${uiState.error}", color = Color.Red)
                        Button(
                            onClick = { rivalViewModel.fetchRivales() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.rivals.isEmpty() -> Text(
                    "No hay rivales registrados",
                    color = TextoGris
                )
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.rivals) { rival ->
                            RivalCardWithActions(
                                rival = rival,
                                onEdit = {
                                    editingRival = rival
                                    inputNombre = rival.nombre ?: ""
                                    showDialog = true
                                },
                                onDelete = { rivalViewModel.deleteRival(rival.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editingRival = null
                inputNombre = ""
            },
            title = {
                Text(
                    if (editingRival == null) "Crear Rival" else "Editar Rival",
                    color = Color.White
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputNombre,
                        onValueChange = { inputNombre = it },
                        label = { Text("Nombre del rival") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = VerdePrincipal,
                            unfocusedLabelColor = TextoGris
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputNombre.isNotBlank()) {
                            if (editingRival == null) {
                                rivalViewModel.createRival(Rival(id = 0, nombre = inputNombre))
                            } else {
                                rivalViewModel.updateRival(
                                    Rival(id = editingRival!!.id, nombre = inputNombre)
                                )
                            }
                            showDialog = false
                            editingRival = null
                            inputNombre = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        editingRival = null
                        inputNombre = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            },
            containerColor = GrisComponente
        )
    }
}

@Composable
fun RivalCardWithActions(rival: Rival, onEdit: () -> Unit, onDelete: () -> Unit) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Flag,
                    contentDescription = null,
                    tint = VerdePrincipal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    rival.nombre ?: "Rival desconocido",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
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