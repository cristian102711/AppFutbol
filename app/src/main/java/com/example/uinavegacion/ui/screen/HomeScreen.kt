@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.BorderStroke // Import
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris // Import
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUserName = authViewModel.currentUserName ?: "Usuario"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                navController = navController,
                authViewModel = authViewModel,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        },
        gesturesEnabled = drawerState.isOpen
    ) {
        Scaffold(
            bottomBar = {
                MyBottomBar(onMenuClick = { scope.launch { drawerState.open() } })
            }
        ) { innerPadding ->
            // --- INICIO DE LA ACTUALIZACIÓN ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cabecera (sin cambios)
                HeaderProfile(userName = currentUserName)

                Spacer(modifier = Modifier.height(48.dp))

                // Botón para "Crear Equipo"
                Button(
                    onClick = {
                        // ¡Aquí está la navegación!
                        navController.navigate(Route.CreateTeam.path)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Crear Equipo", fontWeight = FontWeight.Bold)
                }

                // (Dejamos fuera los botones rotos de "Crear Partido" y "Ver Partidos")
            }
            // --- FIN DE LA ACTUALIZACIÓN ---
        }
    }
}

// HeaderProfile (Sin cambios)
@Composable
fun HeaderProfile(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Hola,", fontSize = 18.sp, color = TextoGris)
            Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
        Box(
            modifier = Modifier.size(50.dp).clip(CircleShape).background(GrisComponente),
            contentAlignment = Alignment.Center
        ) {
            val initials = if (userName.length >= 2) userName.take(2).uppercase() else userName.uppercase()
            Text(text = initials, color = VerdePrincipal, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

// MyBottomBar (Sin cambios)
@Composable
fun MyBottomBar(onMenuClick: () -> Unit) {
    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar Partidos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onMenuClick,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menú") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
    }
}

// AppDrawerContent (Sin cambios)
@Composable
fun AppDrawerContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = GrisComponente) {
        Text(
            "Menú de Opciones",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = TextoGris.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))
        val isGuest = authViewModel.currentUserName == "Invitado"
        val logoutAction = {
            onCloseDrawer()
            authViewModel.logoutUser()
            navController.navigate(Route.Login.path) { popUpTo(0) }
        }
        if (isGuest) {
            NavigationDrawerItem(
                label = { Text("Salir") },
                selected = false,
                onClick = logoutAction,
                icon = { Icon(Icons.Default.DoorBack, contentDescription = "Salir") },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        } else {
            NavigationDrawerItem(
                label = { Text("Cerrar Sesión") },
                selected = false,
                onClick = logoutAction,
                icon = { Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión") },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}