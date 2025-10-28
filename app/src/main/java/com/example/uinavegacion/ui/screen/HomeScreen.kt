@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

// --- IMPORTS NUEVOS PARA PERMISOS ---
import android.Manifest // <-- NUEVO
import android.content.Context
import android.content.pm.PackageManager // <-- NUEVO
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat // <-- NUEVO
// --- FIN IMPORTS NUEVOS ---
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUserName = authViewModel.currentUserName ?: "Usuario"

    var showImageDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var tempCameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // --- LANZADOR 1 (GALERÍA) - Sin cambios
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                authViewModel.onProfileImageSelected(uri)
            }
            showImageDialog = false
        }
    )

    // --- LANZADOR 2 (CÁMARA) - Sin cambios en su lógica
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                authViewModel.onProfileImageSelected(tempCameraImageUri)
            }
            showImageDialog = false
        }
    )

    // --- LANZADOR 3 (PERMISOS) - ¡NUEVO!
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permiso concedido, AHORA lanzamos la cámara
                val newUri = createImageUri(context)
                tempCameraImageUri = newUri
                cameraLauncher.launch(newUri)
            } else {
                // Permiso denegado. Cerramos el pop-up.
                // (Opcional: podrías mostrar un Snackbar de error)
                showImageDialog = false
            }
        }
    )

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                HeaderProfile(
                    userName = currentUserName,
                    profileImageUri = authViewModel.profileImageUri,
                    onProfileClick = {
                        showImageDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Bienvenido.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    text = "Usa el menú lateral para ver todas las opciones.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoGris
                )
            }

            // --- POP-UP CON LÓGICA DE PERMISOS (MODIFICADO) ---
            if (showImageDialog) {
                ImagePickerDialog(
                    onDismiss = { showImageDialog = false },
                    onCameraClick = {
                        // --- LÓGICA DE PERMISOS AÑADIDA ---
                        // 1. Define el permiso que necesitamos
                        val permission = Manifest.permission.CAMERA

                        // 2. Comprueba si ya lo tenemos
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            // 3A. Si SÍ tenemos permiso, lanza la cámara
                            val newUri = createImageUri(context)
                            tempCameraImageUri = newUri
                            cameraLauncher.launch(newUri)
                        } else {
                            // 3B. Si NO tenemos permiso, lanza el pop-up de permiso
                            permissionLauncher.launch(permission)
                        }
                    },
                    onGalleryClick = {
                        // La galería no necesita esta lógica, se lanza directo
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }
}

// --- ImagePickerDialog (SIN CAMBIOS) ---
@Composable
fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GrisComponente)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Cambiar foto de perfil",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCameraClick() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara", tint = VerdePrincipal)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Tomar Foto", color = Color.White, fontSize = 18.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGalleryClick() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Galería", tint = VerdePrincipal)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Elegir de Galería", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

// --- createImageUri (SIN CAMBIOS) ---
private fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(Date())
    val imageFile = File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        context.externalCacheDir
    )
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.example.uinavegacion.provider",
        imageFile
    )
}

// --- MenuItem (SIN CAMBIOS) ---
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false
)

// --- MenuItemRow (SIN CAMBIOS) ---
@Composable
fun MenuItemRow(item: MenuItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (item.isDestructive) Color.Red.copy(alpha = 0.8f) else VerdePrincipal,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            color = if (item.isDestructive) Color.Red.copy(alpha = 0.9f) else Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- HeaderProfile (SIN CAMBIOS) ---
@Composable
fun HeaderProfile(
    userName: String,
    profileImageUri: Uri?,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Hola,", fontSize = 18.sp, color = TextoGris)
            Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(VerdePrincipal)
                .border(2.dp, VerdePrincipal, CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri == null) {
                val initials = if (userName == "Invitado") "IN"
                else if (userName.length >= 2) userName.take(2).uppercase()
                else userName.uppercase()
                Text(
                    text = initials,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            } else {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

// --- MyBottomBar (SIN CAMBIOS) ---
@Composable
fun MyBottomBar(onMenuClick: () -> Unit) {
    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = true, onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = VerdePrincipal, unselectedIconColor = TextoGris, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false, onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar Partidos") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = VerdePrincipal, unselectedIconColor = TextoGris, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false, onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            // --- AQUÍ ESTABA EL ERROR ---
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris, // <-- Corregido (antes TextoGGris)
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false, onClick = onMenuClick,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menú") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = VerdePrincipal, unselectedIconColor = TextoGris, indicatorColor = Color.Transparent)
        )
    }
}
// --- AppDrawerContent (SIN CAMBIOS) ---
@Composable
fun AppDrawerContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    onCloseDrawer: () -> Unit
) {
    val isGuest = authViewModel.currentUserName == "Invitado"
    val currentUserName = authViewModel.currentUserName ?: "Usuario"

    val dynamicMenuItems = buildList {
        add(MenuItem("Ver estadísticas", Icons.Default.Analytics))
        add(MenuItem("Chat equipo", Icons.Default.Chat))
        add(MenuItem("Reservar cancha", Icons.Default.CalendarToday))
        add(MenuItem("Emparejamiento automatico", Icons.Default.Shuffle))
        add(MenuItem("Ver Canchas", Icons.Default.LocationOn))
        add(MenuItem("Crear y Confirmar equipos", Icons.Default.Groups))
        add(MenuItem("Calificar jugadores", Icons.Default.Star))

        if (isGuest) {
            add(MenuItem("Salir", Icons.AutoMirrored.Filled.ExitToApp, isDestructive = false))
        } else {
            add(MenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, isDestructive = false))
        }
    }

    ModalDrawerSheet(drawerContainerColor = GrisComponente) {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            HeaderProfile(
                userName = currentUserName,
                profileImageUri = authViewModel.profileImageUri,
                onProfileClick = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = TextoGris.copy(alpha = 0.5f))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                Text(
                    text = "Menú",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(dynamicMenuItems) { item ->
                MenuItemRow(item = item) {
                    onCloseDrawer()
                    when (item.title) {
                        "Crear y Confirmar equipos" -> navController.navigate(Route.CreateTeam.path)
                        "Cerrar Sesión", "Salir" -> {
                            authViewModel.logoutUser()
                            navController.navigate(Route.Login.path) { popUpTo(0) }
                        }
                        else -> { /* TODO: Navegar a otras pantallas */ }
                    }
                }
            }
        }
    }
}