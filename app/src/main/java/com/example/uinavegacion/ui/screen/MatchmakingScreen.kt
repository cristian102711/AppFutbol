@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- Imports de Tema y Colores ---
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
// ---------------------------------

// --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
import com.example.uinavegacion.navigation.Route
// ---------------------------------

import com.example.uinavegacion.ui.screen.customTextFieldColors
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MatchmakingScreen(navController: NavController) {

    // --- Lista Completa de Comunas (Provincia de Santiago) ---
    val todasLasComunas = listOf(
        "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central",
        "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja",
        "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo",
        "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén",
        "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta",
        "Renca", "San Joaquín", "San Miguel", "San Ramón", "Santiago", "Vitacura"
    )

    // --- Estados para los selectores ---
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") } // <-- Se escribe
    var selectedLocation by remember { mutableStateOf("") } // <-- Se busca

    // Estado para el menú de ubicación
    var isLocationMenuExpanded by remember { mutableStateOf(false) }

    // Lista filtrada de comunas
    val filteredComunas = remember(selectedLocation) {
        if (selectedLocation.isBlank()) {
            todasLasComunas // Mostrar todas si no hay texto
        } else {
            todasLasComunas.filter { it.contains(selectedLocation, ignoreCase = true) }
        }
    }

    // Estado para el calendario (DatePicker)
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val isButtonEnabled = selectedDate.isNotBlank() && selectedTime.isNotBlank() && selectedLocation.isNotBlank()

    // --- Diálogo del Calendario (Sin cambios) ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        selectedDate = sdf.format(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = VerdePrincipal)
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = VerdePrincipal)
                ) { Text("Cancelar") }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.Black,
                titleContentColor = Color.White,
                headlineContentColor = VerdePrincipal,
                weekdayContentColor = TextoGris,
                dayContentColor = Color.White,
                selectedDayContainerColor = VerdePrincipal,
                selectedDayContentColor = Color.Black,
                todayDateBorderColor = VerdePrincipal,
                todayContentColor = VerdePrincipal,
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }
    // --- Fin del Calendario ---

    // --- Interfaz de Usuario (UI) ---
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
                .padding(16.dp)
        ) {
            Text(
                text = "Buscar rival",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- 1. Campo de Fecha (Clickable, abre Calendario) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // <-- El clic va en el Box
            ) {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { /* No hacer nada */ },
                    readOnly = true,
                    label = { Text("Fecha") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. Campo de Hora (Editable por el usuario) ---
            OutlinedTextField(
                value = selectedTime,
                onValueChange = { selectedTime = it }, // <-- El usuario escribe
                label = { Text("Hora (ej: 19:30)") },
                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                colors = customTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone, // Teclado numérico
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. Campo de Ubicación (Buscador Autocompletable) ---
            ExposedDropdownMenuBox(
                expanded = isLocationMenuExpanded,
                onExpandedChange = { isLocationMenuExpanded = !isLocationMenuExpanded }
            ) {
                OutlinedTextField(
                    value = selectedLocation,
                    onValueChange = {
                        selectedLocation = it // <-- ¡Sí puedes escribir aquí!
                        isLocationMenuExpanded = true // Mantener abierto mientras escribe
                    },
                    label = { Text("Ubicación (Comuna)") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLocationMenuExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                // Opciones del menú (filtradas)
                ExposedDropdownMenu(
                    expanded = isLocationMenuExpanded,
                    onDismissRequest = { isLocationMenuExpanded = false },
                    modifier = Modifier.background(GrisComponente)
                ) {
                    filteredComunas.forEach { comuna ->
                        DropdownMenuItem(
                            text = { Text(comuna, color = Color.White) },
                            onClick = {
                                selectedLocation = comuna
                                isLocationMenuExpanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al fondo

            // Botón de Buscar
            Button(
                onClick = {
                    // --- ¡EL BOTÓN YA ESTÁ CORREGIDO! ---
                    navController.navigate(Route.AvailableTeams.path)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.Black
                ),
                enabled = isButtonEnabled // Se activa solo si los 3 campos están llenos
            ) {
                Text("Buscar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}