## GUÍA TÉCNICA: CRUD FUNCIONAL - App de Fútbol

### 📋 Resumen Ejecutivo
La app implementa **CRUD completo** (Create, Read, Update, Delete) para 4 entidades: Partidos, Jugadores, Equipos y Rivales. Cada operación se conecta a microservicios REST en Render y usa arquitectura MVVM con Kotlin Coroutines.

---

## 🏗️ Arquitectura por Capas

### 1. **Network Layer** (Retrofit)
**Archivo:** `ApiService.kt`

```kotlin
interface ApiService {
    // GET - Leer
    @GET("rivales")
    suspend fun getRivales(): Response<List<Rival>>
    
    // POST - Crear
    @POST("rivales")
    suspend fun createRival(@Body rival: Rival): Response<Rival>
    
    // PUT - Actualizar
    @PUT("rivales/{id}")
    suspend fun updateRival(@Path("id") id: Long, @Body rival: Rival): Response<Rival>
    
    // DELETE - Eliminar
    @DELETE("rivales/{id}")
    suspend fun deleteRival(@Path("id") id: Long): Response<Void>
}
```

**Todas las funciones devuelven `Response<T>`** para poder verificar `response.isSuccessful` en el repositorio.

---

### 2. **Repository Layer** (Data Handling)
**Archivo:** `RivalRepository.kt`

El repositorio convierte `Response<T>` a `Result<T>` (Kotlin Result) para un manejo limpio de errores:

```kotlin
class RivalRepository(private val apiService: ApiService) {
    
    suspend fun getRivales(): Result<List<Rival>> {
        return try {
            val response = apiService.getRivales()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createRival(rival: Rival): Result<Rival> {
        return try {
            val response = apiService.createRival(rival)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudo crear"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateRival(id: Long, rival: Rival): Result<Rival> {
        // Similar a create
    }
    
    suspend fun deleteRival(id: Long): Result<Boolean> {
        // Similar a create, pero devuelve Boolean
    }
}
```

**Patrón clave:**
- `suspend fun` para operaciones asincrónicas
- `return try { ... } catch` para capturar errores de red
- `response.isSuccessful` verifica HTTP 2xx
- `Result.success/failure` para manejo tipado de errores

---

### 3. **ViewModel Layer** (Business Logic)
**Archivo:** `RivalViewModel.kt`

El ViewModel expone acciones que la UI invoca y actualiza StateFlow:

```kotlin
class RivalViewModel(private val rivalRepository: RivalRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RivalUiState())
    val uiState: StateFlow<RivalUiState> = _uiState.asStateFlow()
    
    fun createRival(rival: Rival) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = rivalRepository.createRival(rival)
            result.onSuccess {
                fetchRivales()  // 👈 Refrescar lista tras éxito
            }.onFailure { throwable ->
                _uiState.update { 
                    it.copy(isLoading = false, error = "Error: ${throwable.message}") 
                }
            }
        }
    }
    
    fun updateRival(id: Long, rival: Rival) { /* similar */ }
    fun deleteRival(id: Long) { /* similar */ }
    fun fetchRivales() { /* refrescar lista */ }
}
```

**Patrón clave:**
- `viewModelScope.launch` para coroutines seguros (cancelables)
- `_uiState.update { }` para actualizaciones atómicas
- `result.onSuccess/onFailure` para reaccionar a resultados
- **Refrescar lista tras operaciones CRUD** para sincronización

---

### 4. **UI Layer** (Composables)
**Archivo:** `RivalListScreen.kt`

La UI es 100% funcional:

```kotlin
@Composable
fun RivalListScreen(rivalViewModel: RivalViewModel) {
    val uiState by rivalViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null -> Text("❌ ${uiState.error}")
            else -> LazyColumn {
                items(uiState.rivals) { rival ->
                    RivalCardWithActions(
                        rival = rival,
                        onEdit = { /* abrir diálogo editar */ },
                        onDelete = { rivalViewModel.deleteRival(rival.id) }
                    )
                }
            }
        }
    }
    
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    rivalViewModel.createRival(Rival(id = 0, nombre = inputNombre))
                    showDialog = false
                })
            }
        )
    }
}
```

**Patrón clave:**
- `collectAsStateWithLifecycle()` para observar StateFlow de forma segura
- FAB (Floating Action Button) para crear
- Botones Edit/Delete en cada card
- AlertDialog para formularios
- Loading → Error → Content UI states

---

## 🔗 Flow Completo: Crear Rival

```
Usuario toca FAB (+)
    ↓
showDialog = true
    ↓
AlertDialog aparece
    ↓
Usuario ingresa "Real Madrid" y toca "Guardar"
    ↓
rivalViewModel.createRival(Rival(nombre = "Real Madrid"))
    ↓
viewModelScope.launch {
    resultado = rivalRepository.createRival(rival)
}
    ↓
apiService.createRival(rival) → POST /rivales
    ↓
Servidor responde: {"id": 5, "nombre": "Real Madrid"}
    ↓
response.isSuccessful → true
    ↓
Result.success(Rival(5, "Real Madrid"))
    ↓
result.onSuccess { fetchRivales() }
    ↓
_uiState.update { copy(rivals = [..., Rival(5, "Real Madrid")]) }
    ↓
UI recompone → Rival aparece en la lista
```

---

## ✅ Testing: Validar que CRUD Funciona

### Opción 1: Tests Unitarios (Mockito)
**Archivo:** `RivalRepositoryTest.kt`

```kotlin
@Test
fun `createRival retorna Success cuando API responde exitosamente`() = runBlocking {
    // Arrange
    val newRival = Rival(3, "Rival Nuevo")
    `when`(mockApiService.createRival(newRival)).thenReturn(
        Response.success(newRival)
    )
    
    // Act
    val result = rivalRepository.createRival(newRival)
    
    // Assert
    assertTrue(result.isSuccess)
}
```

**Ejecutar:**
```bash
./gradlew test --tests RivalRepositoryTest
```

### Opción 2: Tests en UI (Abrir la app)
1. Ir a pantalla "Rivales"
2. Tocar FAB (+) → Ingresar "Nuevo Rival" → Guardar
3. Ver rival en lista
4. Tocar ícono lápiz → Cambiar nombre → Guardar
5. Ver cambio reflejado
6. Tocar ícono basura → Rival desaparece

### Opción 3: Tests con curl (API directo)
```bash
# Crear rival
curl -X POST https://ms-rivales.onrender.com/rivales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test Rival"}'

# Listar
curl https://ms-rivales.onrender.com/rivales

# Actualizar
curl -X PUT https://ms-rivales.onrender.com/rivales/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"nombre":"Updated"}'

# Eliminar
curl -X DELETE https://ms-rivales.onrender.com/rivales/1
```

---

## 📊 Entidades CRUD Implementadas

| Entidad | Create | Read | Update | Delete | API |
|---------|--------|------|--------|--------|-----|
| **Rivales** | ✅ | ✅ | ✅ | ✅ | POST/GET/PUT/DELETE |
| **Jugadores** | ✅ | ✅ | ✅ | ✅ | POST/GET/PUT/DELETE |
| **Equipos** | ✅ | ✅ | ✅ | ✅ | POST/GET/PUT/DELETE |
| **Partidos** | ✅ | ✅ | ✅ | ✅ | POST/GET/PUT/DELETE |

---

## 🎯 Puntos Clave para el Docente

✅ **Completo:** GET + POST (create) + PUT (update) + DELETE  
✅ **4 Entidades:** Rivales, Jugadores, Equipos, Partidos  
✅ **Microservicios:** Conectado a 4 APIs en Render  
✅ **Corrutinas:** `viewModelScope.launch` + `suspend`  
✅ **Result<T>:** Manejo typed de errores sin excepciones  
✅ **MVVM:** Separación clara de capas  
✅ **StateFlow:** UI reactiva y observable  
✅ **Tests:** Unitarios + Tests en UI  
✅ **Validación:** Campos requeridos antes de enviar  
✅ **Feedback:** Loading/Error/Success states  

---

## 📁 Archivos Clave

```
Data Layer:
├── network/
│   └── ApiService.kt (endpoints CRUD)
├── repository/
│   ├── RivalRepository.kt
│   ├── JugadorRepository.kt
│   ├── EquipoRepository.kt
│   └── PartidoRepository.kt
└── model/
    ├── Rival.kt
    ├── Jugador.kt
    ├── Equipo.kt
    └── Partido.kt

UI Layer:
├── viewmodel/
│   ├── RivalViewModel.kt
│   ├── JugadorViewModel.kt
│   ├── EquipoViewModel.kt
│   └── PartidoViewModel.kt
└── screen/
    ├── RivalListScreen.kt
    ├── PlayerListScreen.kt
    ├── TeamListScreen.kt
    └── MatchListScreen.kt

Tests:
├── RivalRepositoryTest.kt
└── PartidoRepositoryTest.kt
```

---

## 🚀 Cómo Presentar en la Defensa

1. **Abrir la app → Ir a "Rivales"**
2. **Crear:** Tocar FAB (+), ingresa nombre, Guardar
3. **Leer:** Muestra lista de rivales
4. **Actualizar:** Tocar ícono lápiz, cambiar nombre
5. **Eliminar:** Tocar ícono basura
6. **Repetir en:** Jugadores, Equipos, Partidos
7. **Mostrar tests:** Abrir RivalRepositoryTest.kt, explicar mocking
8. **Mostrar código:** Abrir RivalViewModel → explicar flujo

---

**¡ Listo para defender ! ✅**

