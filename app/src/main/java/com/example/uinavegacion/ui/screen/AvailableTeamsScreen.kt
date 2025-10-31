@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.navigation.Route // <-- Importante
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal

// Datos de ejemplo para la lista
data class TeamAvailable(
    val id: Int,
    val name: String,
    val details: String
)

// Usamos datos de ejemplo (sample) por ahora
val sampleTeams = listOf(
    TeamAvailable(1, "Los Galacticos FC", "Ñuñoa - 19:30 hrs"),
    TeamAvailable(2, "Real Amigos FC", "Providencia - 20:00 hrs"),
    TeamAvailable(3, "Atletico Cracks", "Macul - 19:00 hrs"),
    TeamAvailable(4, "Inter de Frutillar", "Santiago - 21:00 hrs")
)

@Composable
fun AvailableTeamsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos Disponibles") },
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

        // Usamos LazyColumn para crear una lista scrolleable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre cada tarjeta
        ) {
            items(sampleTeams) { team ->
                AvailableTeamCard(
                    teamName = team.name,
                    details = team.details,
                    onChallengeClick = {
                        // --- ¡AQUÍ ESTÁ LA ACTUALIZACIÓN! ---
                        // Ahora el botón SÍ navega a la pantalla final
                        navController.navigate(Route.MatchFound.path)
                    }
                )
            }
        }
    }
}

@Composable
fun AvailableTeamCard(
    teamName: String,
    details: String,
    onChallengeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Columna para Nombre y Detalles
            Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                Text(
                    text = teamName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = details,
                    color = TextoGris,
                    fontSize = 14.sp
                )
            }

            // Botón de Desafiar
            Button(
                onClick = onChallengeClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.Black
                )
            ) {
                Text("Desafiar", fontWeight = FontWeight.Bold)
            }
        }
    }
}