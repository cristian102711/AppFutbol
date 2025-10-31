@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.theme.VerdePrincipal

@Composable
fun MatchmakingStartScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emparejamiento Automático") },
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 1. Animación del Radar
            PulsatingRadar()

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Texto "Buscando rival"
            Text(
                text = "Buscando rival disponible...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible

            // 3. Botón "Buscar Rival"
            Button(
                onClick = {
                    // Navega al formulario (Pantalla 2)
                    navController.navigate(Route.Matchmaking.path)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.Black
                )
            ) {
                Text("Buscar rival", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Botón "Cancelar"
            OutlinedButton(
                onClick = { navController.popBackStack() }, // Simplemente vuelve atrás
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, VerdePrincipal),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdePrincipal)
            ) {
                Text("Cancelar emparejamiento", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp)) // Padding inferior
        }
    }
}

@Composable
fun PulsatingRadar() {
    val infiniteTransition = rememberInfiniteTransition(label = "radarTransition")

    // Animación de escala (zoom in/out)
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "radarScale"
    )

    // Animación de opacidad (fade in/out)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "radarAlpha"
    )

    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Círculo exterior (el que pulsa)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale) // Aplica escala
                .background(
                    color = VerdePrincipal.copy(alpha = alpha * 0.3f), // Aplica opacidad
                    shape = CircleShape
                )
        )
        // Círculo interior (el punto fijo)
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = VerdePrincipal, shape = CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        )
    }
}