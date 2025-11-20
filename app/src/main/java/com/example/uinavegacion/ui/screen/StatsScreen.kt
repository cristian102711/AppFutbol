@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal

// Datos para las tarjetas de estadísticas
data class StatItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color = VerdePrincipal
)

@Composable
fun StatsScreen(navController: NavController) {

    // Datos simulados del perfil
    val stats = listOf(
        StatItem("Partidos", "42", Icons.Default.SportsSoccer),
        StatItem("Goles", "15", Icons.Default.SportsSoccer),
        StatItem("MVP", "3", Icons.Default.EmojiEvents),
        StatItem("Calif.", "4.8", Icons.Default.Star)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. Cabecera de Perfil (Foto y Nombre)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(GrisComponente),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = TextoGris
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cristian Developer", // Aquí iría el nombre real
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Delantero | Capitán",
                color = VerdePrincipal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Grilla de Estadísticas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stats) { stat ->
                    StatCard(stat)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Barras de Habilidad (Estética "Videojuego")
            SkillBar(skillName = "Ataque", progress = 0.8f)
            Spacer(modifier = Modifier.height(8.dp))
            SkillBar(skillName = "Defensa", progress = 0.5f)
            Spacer(modifier = Modifier.height(8.dp))
            SkillBar(skillName = "Velocidad", progress = 0.9f)
        }
    }
}

@Composable
fun StatCard(stat: StatItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = GrisComponente),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(stat.icon, contentDescription = null, tint = stat.color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stat.value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = stat.label, color = TextoGris, fontSize = 12.sp)
        }
    }
}

@Composable
fun SkillBar(skillName: String, progress: Float) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = skillName, color = Color.White, fontSize = 14.sp)
            Text(text = "${(progress * 100).toInt()}", color = TextoGris, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = VerdePrincipal,
            trackColor = Color.DarkGray,
        )
    }
}