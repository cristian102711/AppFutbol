@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.viewmodel.CreateTeamViewModel
import com.example.uinavegacion.viewmodel.Player

/**
 * Pantalla principal que decide qué vista mostrar:
 * - El formulario de creación de equipo.
 * - La pantalla de confirmación de equipo.
 */
@Composable
fun CreateTeamScreen(
    navController: NavController,
    viewModel: CreateTeamViewModel
) {
    // Observa el estado 'isConfirmed' del ViewModel
    val isConfirmed by viewModel::isConfirmed

    if (isConfirmed) {
        // --- Muestra la Vista de Confirmación ---
        ConfirmationView(navController = navController, viewModel = viewModel)
    } else {
        // --- Muestra el Formulario de Creación ---
        CreateTeamForm(navController = navController, viewModel = viewModel)
    }
}

/**
 * Vista del Formulario para "Crear equipo"
 */
@Composable
private fun CreateTeamForm(
    navController: NavController,
    viewModel: CreateTeamViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear equipo") },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Permite scroll si el contenido es mucho
        ) {
            // --- Campos del Equipo ---
            StyledTextField(
                value = viewModel.teamName,
                onValueChange = { viewModel.onTeamNameChanged(it) },
                label = "Nombre del equipo",
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(16.dp))
            StyledTextField(
                value = viewModel.teamEmail,
                onValueChange = { viewModel.onTeamEmailChanged(it) },
                label = "Correo electronico",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = GrisComponente)
            Spacer(modifier = Modifier.height(24.dp))

            // --- Sección para Añadir Jugador ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Jugador",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = { viewModel.addPlayer() }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Añadir jugador",
                        tint = VerdePrincipal,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            StyledTextField(
                value = viewModel.playerName,
                onValueChange = { viewModel.onPlayerNameChanged(it) },
                label = "Nombre"
            )
            Spacer(modifier = Modifier.height(16.dp))
            StyledTextField(
                value = viewModel.playerEmail,
                onValueChange = { viewModel.onPlayerEmailChanged(it) },
                label = "Correo",
                keyboardType = KeyboardType.Email
            )

            // --- Lista de Jugadores Añadidos (para que sea funcional) ---
            if (viewModel.players.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Jugadores añadidos:",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoGris
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Mostramos los jugadores en una mini-lista
                viewModel.players.forEach { player ->
                    Text(
                        text = "• ${player.name} (${player.email})",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al fondo
            Spacer(modifier = Modifier.height(32.dp))

            // --- Botón de Confirmar ---
            Button(
                onClick = { viewModel.confirmTeam() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.Black
                )
            ) {
                Text("Confirmar equipo", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Vista de Confirmación para "¡Equipo confirmado!"
 */
@Composable
private fun ConfirmationView(
    navController: NavController,
    viewModel: CreateTeamViewModel
) {
    Scaffold(
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Separa el contenido
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(60.dp))
                // --- Icono de Check ---
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(VerdePrincipal),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Confirmado",
                        tint = Color.Black,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "¡Equipo confirmado!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tu equipo está listo para jugar",
                    fontSize = 16.sp,
                    color = TextoGris
                )
                Spacer(modifier = Modifier.height(32.dp))

                // --- Lista de Jugadores ---
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp) // Limita la altura de la lista
                        .clip(RoundedCornerShape(8.dp))
                        .background(GrisComponente)
                ) {
                    items(viewModel.players) { player ->
                        PlayerRowItem(player = player)
                    }
                }
            }

            // --- Botones Inferiores ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        viewModel.resetAndFinish()
                        navController.popBackStack() // Vuelve a la pantalla anterior (Home)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Listo", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { viewModel.editTeam() }) {
                    Text("Editar equipo", color = TextoGris)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Un Composable reutilizable para los TextFields, estilizado
 * como en el Login/Registro.
 */
@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null, tint = TextoGris) }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = VerdePrincipal,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = GrisComponente,
            unfocusedContainerColor = GrisComponente,
            cursorColor = VerdePrincipal,
            focusedLabelColor = TextoGris,
            unfocusedLabelColor = TextoGris,
            focusedLeadingIconColor = VerdePrincipal,
            unfocusedLeadingIconColor = TextoGris,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

/**
 * Un Composable para mostrar una fila de jugador en la lista de confirmación.
 */
@Composable
private fun PlayerRowItem(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Icono de jugador",
                tint = VerdePrincipal
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = player.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = player.email,
                color = TextoGris,
                fontSize = 14.sp
            )
        }
    }
}