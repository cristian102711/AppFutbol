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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.JugadorUiState
import com.example.uinavegacion.ui.viewmodel.JugadorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListScreen(jugadorViewModel: JugadorViewModel) {
    val uiState by jugadorViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var editingJugador by remember { mutableStateOf<Jugador?>(null) }
    var inputNombre by remember { mutableStateOf("") }
    var inputPosicion by remember { mutableStateOf("") }
    var inputDorsal by remember { mutableStateOf("") }
    var inputEdad by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jugadores - CRUD") },
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
                    editingJugador = null
                    inputNombre = ""
                    inputPosicion = ""
                    inputDorsal = ""
                    inputEdad = ""
                },
                containerColor = VerdePrincipal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear jugador")
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
            PlayerListContent(uiState = uiState, onEdit = { jugador ->
                editingJugador = jugador
                inputNombre = jugador.nombre ?: ""
                inputPosicion = jugador.posicion ?: ""
                inputDorsal = (jugador.dorsal ?: 0).toString()
                inputEdad = (jugador.edad ?: 0).toString()
                showDialog = true
            }, onDelete = { jugadorViewModel.deleteJugador(it) })
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editingJugador = null
            },
            title = {
                Text(
                    if (editingJugador == null) "Crear Jugador" else "Editar Jugador",
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = inputNombre,
                        onValueChange = { inputNombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            focusedLabelColor = VerdePrincipal
                        )
                    )
                    OutlinedTextField(
                        value = inputPosicion,
                        onValueChange = { inputPosicion = it },
                        label = { Text("Posición") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            focusedLabelColor = VerdePrincipal
                        )
                    )
                    OutlinedTextField(
                        value = inputDorsal,
                        onValueChange = { inputDorsal = it },
                        label = { Text("Dorsal") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            focusedLabelColor = VerdePrincipal
                        )
                    )
                    OutlinedTextField(
                        value = inputEdad,
                        onValueChange = { inputEdad = it },
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
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
                        if (inputNombre.isNotBlank()) {
                            val jugador = Jugador(
                                id = editingJugador?.id ?: 0,
                                nombre = inputNombre,
                                posicion = inputPosicion,
                                dorsal = inputDorsal.toIntOrNull(),
                                edad = inputEdad.toIntOrNull(),
                                equipoId = editingJugador?.equipoId ?: 1
                            )
                            if (editingJugador == null) {
                                jugadorViewModel.createJugador(jugador)
                            } else {
                                jugadorViewModel.updateJugador(jugador)
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
                Button(
                    onClick = {
                        showDialog = false
                        editingJugador = null
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
private fun PlayerListContent(
    uiState: JugadorUiState,
    onEdit: (Jugador) -> Unit,
    onDelete: (Long) -> Unit
) {
    when {
        uiState.isLoading -> {
            CircularProgressIndicator(color = VerdePrincipal)
        }
        uiState.error != null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "❌ ${uiState.error}",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
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
                    PlayerCardWithActions(
                        jugador = jugador,
                        onEdit = { onEdit(jugador) },
                        onDelete = { onDelete(jugador.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerCardWithActions(jugador: Jugador, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jugador.nombre ?: "Nombre desconocido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Posición: ${jugador.posicion ?: "n/a"}",
                    fontSize = 16.sp,
                    color = TextoGris
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Edad: ${jugador.edad ?: 0} | Equipo: ${jugador.equipoId ?: "?"}",
                    fontSize = 14.sp,
                    color = TextoGris
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(VerdePrincipal.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = if ((jugador.dorsal ?: 0) > 0) "#${jugador.dorsal}" else "-",
                        color = VerdePrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
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