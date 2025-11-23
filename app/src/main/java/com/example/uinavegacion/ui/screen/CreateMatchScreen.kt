package com.example.uinavegacion.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.uinavegacion.ui.theme.GrisComponente
import com.example.uinavegacion.ui.theme.TextoGris
import com.example.uinavegacion.ui.theme.VerdePrincipal
import com.example.uinavegacion.ui.viewmodel.CreateMatchViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(navController: NavController, viewModel: CreateMatchViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // --- Estados del Formulario ---
    var matchName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedFormat by remember { mutableStateOf("") }
    var isFormatExpanded by remember { mutableStateOf(false) }
    val formats = listOf("Fútbol 5 (5v5)", "Fútbol 7 (7v7)", "Fútbol 11 (11v11)")

    val isButtonEnabled = matchName.isNotBlank() && location.isNotBlank() && selectedDate.isNotBlank() && !uiState.isLoading

    // --- Efecto para reaccionar a cambios de estado (éxito/error) ---
    LaunchedEffect(uiState) {
        if (uiState.success) {
            Toast.makeText(context, "¡Partido creado con éxito!", Toast.LENGTH_SHORT).show()
            viewModel.resetState() // Limpia el estado
            navController.popBackStack() // Vuelve a la pantalla anterior
        }
        uiState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            viewModel.resetState() // Limpia el estado del error
        }
    }

    // --- Diálogo del Calendario ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let {
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            selectedDate = sdf.format(it)
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = VerdePrincipal)
                ) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Partido") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                // --- FORMULARIO ---
                OutlinedTextField(value = matchName, onValueChange = { matchName = it }, label = { Text("Nombre del Partido") }, leadingIcon = { Icon(Icons.Default.SportsSoccer, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(16.dp))
                ExposedDropdownMenuBox(expanded = isFormatExpanded, onExpandedChange = { isFormatExpanded = !isFormatExpanded }) {
                    OutlinedTextField(value = selectedFormat, onValueChange = {}, readOnly = true, label = { Text("Formato") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFormatExpanded) }, leadingIcon = { Icon(Icons.Default.Groups, null) }, modifier = Modifier.fillMaxWidth().menuAnchor())
                    ExposedDropdownMenu(expanded = isFormatExpanded, onDismissRequest = { isFormatExpanded = false }) {
                        formats.forEach { format -> DropdownMenuItem(text = { Text(format) }, onClick = { selectedFormat = format; isFormatExpanded = false }) }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = selectedDate, onValueChange = {}, readOnly = true, label = { Text("Fecha") }, leadingIcon = { Icon(Icons.Default.CalendarToday, null) }, modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }, enabled = false)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Hora (Ej: 20:00)") }, leadingIcon = { Icon(Icons.Default.Schedule, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ubicación") }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                
                Spacer(modifier = Modifier.weight(1f))

                // --- BOTÓN DE ACCIÓN ---
                Button(
                    onClick = {
                        viewModel.createPartido(
                            nombreRival = matchName, // Adaptación temporal
                            fecha = "$selectedDate - $time",
                            resultado = "Pendiente" // Valor por defecto
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = isButtonEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color.Black)
                ) {
                    Text("Crear Partido", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // --- INDICADOR DE CARGA ---
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = VerdePrincipal)
            }
        }
    }
}
