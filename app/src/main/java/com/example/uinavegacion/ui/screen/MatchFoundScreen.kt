@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal

@Composable
fun MatchFoundScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emparejamiento Exitoso") },
                navigationIcon = {
                    // Llevamos al Home, no solo "atrás"
                    IconButton(onClick = {
                        navController.navigate(Route.Home.path) {
                            popUpTo(Route.Home.path) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al Home")
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "¡Rival Encontrado!",
                color = VerdePrincipal,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Fila de Logos de Equipos (usamos placeholders)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder Logo Equipo 1
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(GrisComponente)
                        .border(2.dp, VerdePrincipal, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("TU", color = VerdePrincipal, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Text("VS", color = TextoGris, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                // Placeholder Logo Equipo 2
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(GrisComponente)
                        .border(2.dp, Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("RIVAL", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombres de Equipos
            Text(
                text = "Tu Equipo vs. Los Galacticos FC", // Datos de ejemplo
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Detalles del Partido
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrisComponente, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Detalles del Partido:",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Viernes 5 de junio - 19:30 hrs\nFutbolito Ñuñoa, Pedro de Valdivia", // Datos de ejemplo
                    color = TextoGris,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible

            // Botones de Acción
            Button(
                onClick = { /* TODO: Navegar a la pantalla de Chat */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.Black
                )
            ) {
                Text("Ir al Chat", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Home.path) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, VerdePrincipal),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdePrincipal)
            ) {
                Text("Volver al Inicio", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}