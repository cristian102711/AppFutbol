@file:OptIn(ExperimentalMaterial3Api::class) // Needed for Material 3 components

package com.example.uinavegacion.ui.screen // Make sure the package matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Import common icons
import androidx.compose.material.icons.filled.Logout // Specific import for Logout icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope // Needed to open/close drawer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // To make the avatar circular
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController // Needed for navigation
import com.example.uinavegacion.navigation.Route // Import our defined routes
import com.example.uinavegacion.ui.theme.GrisComponente // Import custom colors
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.viewmodel.AuthViewModel // Import our ViewModel
import kotlinx.coroutines.launch // Needed to launch coroutines for drawer

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {

    // 1. State for controlling the side drawer (menu)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // 2. Coroutine scope to manage opening/closing the drawer smoothly
    val scope = rememberCoroutineScope()

    // 3. ModalNavigationDrawer provides the side menu functionality
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // This composable defines what appears INSIDE the drawer
            AppDrawerContent(
                navController = navController,
                authViewModel = authViewModel,
                onCloseDrawer = { // Pass a function to close the drawer from within
                    scope.launch { drawerState.close() }
                }
            )
        },
        gesturesEnabled = drawerState.isOpen // Allow swiping only when open (optional)
    ) {
        // 4. Scaffold is the main layout for the screen content (when drawer is closed)
        Scaffold(
            // 5. Bottom Navigation Bar
            bottomBar = {
                MyBottomBar(
                    onMenuClick = {
                        // Action to open the drawer when the menu icon is clicked
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { innerPadding ->
            // 6. Main content area of the Home screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Apply padding from Scaffold (respects bottom bar)
                    .padding(16.dp)        // Apply our own padding
            ) {
                // Header section ("Hola, Cristian" + Avatar)
                HeaderProfile()
                Spacer(modifier = Modifier.height(32.dp))

                // Placeholder for the main app content (e.g., list of matches)
                Text(
                    text = "Contenido Principal de la App",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Add more UI elements here as needed...
            }
        } // End of Scaffold
    } // End of ModalNavigationDrawer
}


/** Composable for the header section. */
@Composable
fun HeaderProfile() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Hola,", fontSize = 18.sp, color = TextoGris)
            // TODO: Replace "Cristian" with the actual user's name later
            Text(text = "Cristian", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
        // Circular Avatar placeholder
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape) // Makes the Box circular
                .background(GrisComponente),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Replace "CV" with actual user initials later
            Text(text = "CV", color = VerdePrincipal, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}


/** Composable for the Bottom Navigation Bar. */
@Composable
fun MyBottomBar(onMenuClick: () -> Unit) {
    NavigationBar(
        containerColor = Color.Black // Explicitly set background to black
    ) {
        // Home Item (selected by default)
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO: Navigate to Home section if needed */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal, // Green when selected
                unselectedIconColor = TextoGris,    // Gray when not selected
                indicatorColor = Color.Transparent // No background indicator circle
            )
        )
        // Search/Matches Item
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO: Navigate to Search/Matches section */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar Partidos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
        // Notifications Item
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO: Navigate to Notifications section */ },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
        // Menu Item (opens the drawer)
        NavigationBarItem(
            selected = false,
            onClick = onMenuClick, // Call the lambda passed from HomeScreen
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menú") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
                indicatorColor = Color.Transparent
            )
        )
    }
}


/** Composable for the content inside the Navigation Drawer. */
@Composable
fun AppDrawerContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    onCloseDrawer: () -> Unit // Function to close the drawer
) {
    // Provides the standard drawer sheet layout
    ModalDrawerSheet(
        drawerContainerColor = GrisComponente // Dark gray background for the drawer
    ) {
        Text(
            "Menú de Opciones",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = TextoGris.copy(alpha = 0.5f)) // Faint divider line
        Spacer(modifier = Modifier.height(16.dp))

        // --- Drawer Items ---

        // Logout Item
        NavigationDrawerItem(
            label = { Text("Cerrar Sesión") },
            selected = false, // This item is never "selected"
            onClick = {
                onCloseDrawer() // Close the drawer first
                // Perform logout actions: clear fields and navigate
                authViewModel.clearLoginFields()
                navController.navigate(Route.Login.path) {
                    popUpTo(0) // Clear the entire navigation history
                }
            },
            icon = { Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión") },
            colors = NavigationDrawerItemDefaults.colors( // Custom colors for dark theme
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Add more drawer items here if needed (e.g., Profile, Settings)
        /*
        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = false,
            onClick = { /* TODO: Navigate to Profile */ onCloseDrawer() },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
             colors = ... // Apply custom colors
        )
        */

    }
}