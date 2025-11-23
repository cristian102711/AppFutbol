package com.example.uinavegacion.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.PartidoUiState
import com.example.uinavegacion.ui.viewmodel.PartidoViewModel
import com.example.uinavegacion.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, 
    authViewModel: AuthViewModel, 
    partidoViewModel: PartidoViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUserName = authViewModel.currentUserName ?: "Usuario"
    val uiState by partidoViewModel.uiState.collectAsStateWithLifecycle()

    var showImageDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var tempCameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                authViewModel.onProfileImageSelected(uri)
            }
            showImageDialog = false
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                authViewModel.onProfileImageSelected(tempCameraImageUri)
            }
            showImageDialog = false
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val newUri = createImageUri(context)
                tempCameraImageUri = newUri
                cameraLauncher.launch(newUri)
            } else {
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
                    onProfileClick = { showImageDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtonsRow(navController = navController)

                Spacer(modifier = Modifier.height(24.dp))

                UpcomingMatchesSection(uiState = uiState)

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (showImageDialog) {
                ImagePickerDialog(
                    onDismiss = { showImageDialog = false },
                    onCameraClick = {
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            val newUri = createImageUri(context)
                            tempCameraImageUri = newUri
                            cameraLauncher.launch(newUri)
                        } else {
                            permissionLauncher.launch(permission)
                        }
                    },
                    onGalleryClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun UpcomingMatchesSection(uiState: PartidoUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Próximos partidos",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(color = VerdePrincipal)
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
                uiState.partidos.isEmpty() -> {
                    Text(
                        text = "No hay partidos programados.",
                        color = TextoGris,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(uiState.partidos) { partido ->
                            MatchCard(
                                teamName = partido.nombreRival,
                                details = "Fecha: ${partido.fecha} - Resultado: ${partido.resultado}",
                                buttonText = "ver detalles",
                                isConfirmation = false,
                                onClick = { /* TODO: Navegar a detalles del partido con el partido.id */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtonsRow(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton(
            text = "Crear partido",
            onClick = { navController.navigate(Route.CreateMatch.path) },
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            text = "Emparejamiento",
            onClick = { navController.navigate(Route.MatchmakingStart.path) },
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            text = "Reservar cancha",
            onClick = { navController.navigate(Route.Booking.path) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, VerdePrincipal),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdePrincipal)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun MatchCard(
    teamName: String,
    details: String,
    buttonText: String,
    isConfirmation: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrisComponente)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = teamName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = details,
                color = TextoGris,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                colors = if (isConfirmation) {
                    ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.Black
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = VerdePrincipal
                    )
                },
                border = if (!isConfirmation) BorderStroke(1.dp, VerdePrincipal) else null
            ) {
                Text(buttonText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

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

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false
)

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

@Composable
fun MyBottomBar(onMenuClick: () -> Unit) {
    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = true, onClick = { /* Ya estamos en Home */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = VerdePrincipal, unselectedIconColor = TextoGris, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false, onClick = { /* TODO: Navegar a Buscar */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar Partidos") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = VerdePrincipal, unselectedIconColor = TextoGris, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false, onClick = { /* TODO: Navegar a Notificaciones */ },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VerdePrincipal,
                unselectedIconColor = TextoGris,
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

@Composable
fun AppDrawerContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    onCloseDrawer: () -> Unit
) {
    val isGuest = authViewModel.currentUserName == "Invitado"
    val currentUserName = authViewModel.currentUserName ?: "Usuario"

    val dynamicMenuItems = buildList {
        add(MenuItem("Mi Perfil", Icons.Default.Person))
        add(MenuItem("Ver Jugadores", Icons.Default.SportsSoccer))
        add(MenuItem("Ver Equipos", Icons.Default.Groups)) // <-- AÑADIDO
        add(MenuItem("Emparejamiento automatico", Icons.Default.Shuffle))
        add(MenuItem("Ver Canchas", Icons.Default.LocationOn))
        add(MenuItem("Reservar cancha", Icons.Default.CalendarToday))
        add(MenuItem("Chat equipo", Icons.AutoMirrored.Filled.Chat))

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
                        "Mi Perfil" -> navController.navigate(Route.Stats.path)
                        "Ver Jugadores" -> navController.navigate(Route.PlayerList.path)
                        "Ver Equipos" -> navController.navigate(Route.TeamList.path) // <-- AÑADIDO
                        "Crear y Confirmar equipos" -> navController.navigate(Route.CreateTeam.path)
                        "Emparejamiento automatico" -> navController.navigate(Route.MatchmakingStart.path)
                        "Ver Canchas" -> navController.navigate(Route.CourtList.path)
                        "Chat equipo" -> navController.navigate(Route.Chat.path)
                        "Reservar cancha" -> navController.navigate(Route.Booking.path)

                        "Cerrar Sesión", "Salir" -> {
                            authViewModel.logoutUser()
                            navController.navigate(Route.Login.path) { popUpTo(0) }
                        }
                        else -> { /* No hacer nada */ }
                    }
                }
            }
        }
    }
}