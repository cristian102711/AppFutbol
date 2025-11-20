@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal

// Modelo de datos simple para un mensaje
data class Message(
    val id: String,
    val text: String,
    val isMine: Boolean,
    val time: String
)

@Composable
fun ChatScreen(navController: NavController) {

    // Estado: Lista de mensajes (Simulamos una conversación previa)
    val messages = remember { mutableStateListOf(
        Message("1", "Hola, ¿sigue en pie el partido?", false, "19:00"),
        Message("2", "¡Sí! Confirmados los 5.", true, "19:02"),
        Message("3", "Perfecto, nos vemos en la cancha.", false, "19:05")
    ) }

    // Estado: Texto que el usuario está escribiendo
    var textState by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Los Galacticos FC", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("En línea", fontSize = 12.sp, color = VerdePrincipal)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Lista de Mensajes (Ocupa todo el espacio disponible)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                reverseLayout = false // Los mensajes nuevos abajo
            ) {
                items(messages) { message ->
                    MessageBubble(message)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // 2. Barra de Entrada de Texto (Input)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrisComponente)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text("Escribe un mensaje...") },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Black, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = customTextFieldColors(), // Reutilizamos tu estilo
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Botón Enviar
                IconButton(
                    onClick = {
                        if (textState.isNotBlank()) {
                            // Agregar mensaje a la lista
                            messages.add(Message(
                                id = System.currentTimeMillis().toString(),
                                text = textState,
                                isMine = true, // Es mi mensaje
                                time = "Ahora"
                            ))
                            textState = "" // Limpiar campo
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(VerdePrincipal)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val bubbleColor = if (message.isMine) VerdePrincipal else GrisComponente
    val textColor = if (message.isMine) Color.Black else Color.White
    val alignment = if (message.isMine) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isMine) 16.dp else 0.dp, // "Colita" del globo
                        bottomEnd = if (message.isMine) 0.dp else 16.dp
                    )
                )
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Text(text = message.text, color = textColor, fontSize = 16.sp)
        }
        Text(
            text = message.time,
            color = TextoGris,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
        )
    }
}