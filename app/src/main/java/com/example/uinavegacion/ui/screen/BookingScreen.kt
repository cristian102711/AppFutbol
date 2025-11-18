@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange // <-- Ícono Seguro
import androidx.compose.material.icons.filled.Place     // <-- Ícono Seguro
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.screen.customTextFieldColors // Asegúrate de tener esto importado
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun BookingScreen(navController: NavController) {

    // --- Datos de Ejemplo ---
    val courts = listOf("Cancha 1 (Techada)", "Cancha 2 (Aire Libre)", "Cancha 3 (Futbolito)")
    val timeSlots = listOf(
        "09:00", "10:00", "11:00", "12:00",
        "16:00", "17:00", "18:00", "19:00",
        "20:00", "21:00", "22:00", "23:00"
    )

    // --- Estados ---
    var selectedCourt by remember { mutableStateOf("") }
    var isCourtMenuExpanded by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var selectedTimeSlot by remember { mutableStateOf<String?>(null) }

    val isButtonEnabled = selectedCourt.isNotBlank() && selectedDate.isNotBlank() && selectedTimeSlot != null

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservar Cancha") },
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

            // 1. Selector de Cancha
            Text("Selecciona la cancha:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = isCourtMenuExpanded,
                onExpandedChange = { isCourtMenuExpanded = !isCourtMenuExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCourt,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Elige una cancha") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCourtMenuExpanded) },
                    // CAMBIO: Usamos Place en vez de SportsSoccer
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = isCourtMenuExpanded,
                    onDismissRequest = { isCourtMenuExpanded = false },
                    modifier = Modifier.background(GrisComponente)
                ) {
                    courts.forEach { court ->
                        DropdownMenuItem(
                            text = { Text(court, color = Color.White) },
                            onClick = {
                                selectedCourt = court
                                isCourtMenuExpanded = false
                            },
                            colors = MenuDefaults.itemColors(textColor = Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Selector de Fecha
            Text("Selecciona la fecha:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("DD/MM/AAAA") },
                    // CAMBIO: Usamos DateRange
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Selector de Hora (Cuadrícula)
            Text("Horarios Disponibles:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(timeSlots) { time ->
                    val isSelected = (time == selectedTimeSlot)

                    OutlinedButton(
                        onClick = { selectedTimeSlot = time },
                        shape = RoundedCornerShape(8.dp),
                        border = if (isSelected) BorderStroke(1.dp, VerdePrincipal) else BorderStroke(1.dp, TextoGris),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) VerdePrincipal else Color.Transparent,
                            contentColor = if (isSelected) Color.Black else Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(time, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Botón Confirmar
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.Black
                ),
                enabled = isButtonEnabled
            ) {
                Text("Confirmar Reserva", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}