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

import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris

import com.example.uinavegacion.navigation.Route
import com.example.uinavegacion.ui.viewmodel.MatchmakingViewModel

import java.text.SimpleDateFormat
import java.util.Locale

// NOTA: He borrado la función 'customTextFieldColors()' de aquí.
// Al borrarla, el código usará automáticamente la que está en CommonComponents.kt
// y se arreglarán los 10 errores de "Overload resolution".

@Composable
fun MatchmakingScreen(
    navController: NavController,
    viewModel: MatchmakingViewModel
) {

    // --- Lista Completa de Comunas ---
    val todasLasComunas = listOf(
        "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central",
        "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja",
        "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo",
        "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén",
        "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta",
        "Renca", "San Joaquín", "San Miguel", "San Ramón", "Santiago", "Vitacura"
    )

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("") }
    var isLocationMenuExpanded by remember { mutableStateOf(false) }

    val filteredComunas = remember(selectedLocation) {
        if (selectedLocation.isBlank()) todasLasComunas
        else todasLasComunas.filter { it.contains(selectedLocation, ignoreCase = true) }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val isButtonEnabled = selectedDate.isNotBlank() && selectedTime.isNotBlank() && selectedLocation.isNotBlank()

    // --- Diálogo del Calendario ---
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
                TextButton(onClick = { showDatePicker = false }, colors = ButtonDefaults.textButtonColors(contentColor = VerdePrincipal)) { Text("Cancelar") }
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
        ) { DatePicker(state = datePickerState) }
    }

    // --- UI Principal ---
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

            // 1. Campo Fecha
            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Fecha") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors(), // <--- Usa la función de CommonComponents
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Campo Hora
            OutlinedTextField(
                value = selectedTime,
                onValueChange = { selectedTime = it },
                label = { Text("Hora (ej: 19:30)") },
                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                colors = customTextFieldColors(), // <--- Usa la función de CommonComponents
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Campo Ubicación
            ExposedDropdownMenuBox(
                expanded = isLocationMenuExpanded,
                onExpandedChange = { isLocationMenuExpanded = !isLocationMenuExpanded }
            ) {
                OutlinedTextField(
                    value = selectedLocation,
                    onValueChange = { selectedLocation = it; isLocationMenuExpanded = true },
                    label = { Text("Ubicación (Comuna)") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLocationMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = customTextFieldColors(), // <--- Usa la función de CommonComponents
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                ExposedDropdownMenu(
                    expanded = isLocationMenuExpanded,
                    onDismissRequest = { isLocationMenuExpanded = false },
                    modifier = Modifier.background(GrisComponente)
                ) {
                    filteredComunas.forEach { comuna ->
                        DropdownMenuItem(
                            text = { Text(comuna, color = Color.White) },
                            onClick = { selectedLocation = comuna; isLocationMenuExpanded = false },
                            colors = MenuDefaults.itemColors(textColor = Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Buscar
            Button(
                onClick = {
                    viewModel.startMatchmaking()
                    navController.navigate(Route.MatchFound.path)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color.Black),
                enabled = isButtonEnabled
            ) {
                Text("Buscar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}