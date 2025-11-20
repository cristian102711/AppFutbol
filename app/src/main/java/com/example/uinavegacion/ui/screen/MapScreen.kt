package com.example.uinavegacion.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.uinavegacion.R // Asegúrate de que este import sea de TU paquete
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current

    // Configuración obligatoria de OSM
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
    Configuration.getInstance().userAgentValue = context.packageName

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubicación de Canchas") },
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

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    // Centrar en Santiago
                    val santiagoPoint = GeoPoint(-33.4489, -70.6693)
                    controller.setZoom(12.5)
                    controller.setCenter(santiagoPoint)

                    // --- CREAR MARCADORES ---

                    // Marcador 1: Ñuñoa
                    addCustomMarker(
                        mapView = this,
                        context = context,
                        title = "Futbolito Ñuñoa",
                        snippet = "Toca para ir",
                        position = GeoPoint(-33.4533, -70.6022)
                    )

                    // Marcador 2: Macul
                    addCustomMarker(
                        mapView = this,
                        context = context,
                        title = "Canchas Macul",
                        snippet = "Toca para ir",
                        position = GeoPoint(-33.4947, -70.5992)
                    )

                    // Marcador 3: La Florida (El nuevo que agregamos a la lista)
                    addCustomMarker(
                        mapView = this,
                        context = context,
                        title = "Estadio La Florida",
                        snippet = "Toca para ir",
                        position = GeoPoint(-33.5194, -70.5580)
                    )
                }
            }
        )
    }
}

// --- FUNCIÓN AUXILIAR PARA NO REPETIR CÓDIGO ---
// Esta función crea el marcador, le pone el ícono y le agrega el click de navegación
fun addCustomMarker(
    mapView: MapView,
    context: Context,
    title: String,
    snippet: String,
    position: GeoPoint
) {
    val marker = Marker(mapView)
    marker.position = position
    marker.title = title
    marker.snippet = snippet
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

    // 1. IDEA 2: ÍCONO PERSONALIZADO ⚽
    // Usamos el ícono que creamos. Si no existe, no falla, usa el default.
    // CAMBIAMOS 'ic_soccer_ball' POR TU NOMBRE REAL 'baseline_sports_soccer_24'
    val icon = ContextCompat.getDrawable(context, R.drawable.baseline_sports_soccer_24)
    if (icon != null) {
        marker.icon = icon
    }

    // 2. IDEA 1: NAVEGACIÓN GPS 🚗
    // Al hacer clic en el marcador...
    marker.setOnMarkerClickListener { _, _ ->
        // Mostramos el globito de información primero
        marker.showInfoWindow()

        // Creamos un cuadro de diálogo nativo preguntando si quiere ir
        android.app.AlertDialog.Builder(context)
            .setTitle("Ir a $title")
            .setMessage("¿Quieres abrir el GPS para llegar a esta cancha?")
            .setPositiveButton("Sí, vamos") { dialog, _ ->
                // Lanzamos Google Maps / Waze
                val uri = Uri.parse("google.navigation:q=${position.latitude},${position.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                mapIntent.setPackage("com.google.android.apps.maps") // Intentamos forzar Google Maps

                // Verificamos si tiene la app instalada, si no, lanzamos genérico
                try {
                    context.startActivity(mapIntent)
                } catch (e: Exception) {
                    // Si no tiene Google Maps, abrimos el navegador web u otra app de mapas
                    val webIntent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(webIntent)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        true // Retornamos true para decir que "ya manejamos el evento clic"
    }

    mapView.overlays.add(marker)
}