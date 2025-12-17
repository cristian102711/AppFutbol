# Nombre del Proyecto: UINavegacion (App de Fútbol)

## Integrantes
1. Cristian Velasquez

## Funcionalidades Implementadas ✅

### CRUD Completo (Create, Read, Update, Delete)
- **Gestión de Partidos:** Crear, listar, actualizar y eliminar partidos (CRUD completo).
- **Gestión de Jugadores:** Crear, listar, actualizar y eliminar jugadores.
- **Gestión de Equipos:** Crear, listar, actualizar y eliminar equipos.
- **Gestión de Rivales:** Crear, listar, actualizar y eliminar rivales.
- **Matchmaking:** Sistema de búsqueda de rivales con simulación de espera y selección aleatoria.
- **Microservicios:** Conexión a 4 servicios REST en Render (Equipos, Jugadores, Partidos, Rivales).
- **Persistencia:** Base de datos local (Room) para soporte offline.

## Arquitectura (MVVM + Clean Architecture)

```
Data Layer
├── Network (Retrofit ApiService con GET/POST/PUT/DELETE)
├── Repository (Manejo de errores y conversión a Result<T>)
└── Model (Data Classes)

UI Layer
├── ViewModel (StateFlow para observación reactiva)
├── Screen (Composables con CRUD UI)
└── Theme (Colores y estilos)
```

## Endpoints Utilizados

### Base URLs (Microservicios en Render)
- **Equipos:** `https://ms-equipos.onrender.com/`
- **Jugadores:** `https://ms-jugadores.onrender.com/`
- **Partidos:** `https://ms-partidos.onrender.com/`
- **Rivales:** `https://ms-rivales.onrender.com/`

### Operaciones CRUD Implementadas

**GET (Leer)**
```
GET /equipos
GET /jugadores
GET /partidos
GET /rivales
```

**POST (Crear)**
```
POST /equipos (Body: {"nombre": "...", "entrenador": "...", "escudoUrl": "..."})
POST /jugadores (Body: {"nombre": "...", "posicion": "...", "dorsal": 7, "edad": 25, "equipoId": 1})
POST /partidos (Body: {"fecha": "2024-12-16", "rivalId": 1, "resultado": "GANADO", "golesFavor": 3, "golesContra": 1})
POST /rivales (Body: {"nombre": "..."})
```

**PUT (Actualizar)**
```
PUT /equipos/{id} (Body: Equipo completo)
PUT /jugadores/{id} (Body: Jugador completo)
PUT /partidos/{id} (Body: Partido completo)
PUT /rivales/{id} (Body: Rival completo)
```

**DELETE (Eliminar)**
```
DELETE /equipos/{id}
DELETE /jugadores/{id}
DELETE /partidos/{id}
DELETE /rivales/{id}
```

## Pantallas CRUD Funcionales

### Pantalla de Rivales (RivalListScreen)
- **Listar rivales:** Muestra todos los rivales obtenidos de la API
- **Crear rival:** FAB (Floating Action Button) abre diálogo con campo "Nombre"
- **Editar rival:** Ícono de lápiz permite editar nombre
- **Eliminar rival:** Ícono de basura elimina rival
- **Feedback:** Muestra loading, errores y mensaje de éxito

### Pantalla de Jugadores (PlayerListScreen)
- **Listar jugadores:** Muestra nombre, posición, dorsal y edad
- **Crear jugador:** FAB abre diálogo con campos: nombre, posición, dorsal, edad
- **Editar jugador:** Permite cambiar todos los campos
- **Eliminar jugador:** Botón elimina jugador
- **UI Mejorada:** Círculo con dorsal (#número) y detalles compactos

### Pantalla de Equipos (TeamListScreen)
- **Listar equipos:** Muestra nombre y entrenador
- **Crear equipo:** FAB abre diálogo con campos: nombre, entrenador, URL del escudo
- **Editar equipo:** Permite actualizar información
- **Eliminar equipo:** Botón elimina equipo

### Pantalla de Partidos (MatchListScreen) - NUEVA
- **Listar partidos:** Muestra rival, marcador (goles) y fecha
- **Crear partido:** FAB abre diálogo con campos: fecha, resultado, goles favor, goles contra, ID rival
- **Editar partido:** Permite cambiar detalles del partido
- **Eliminar partido:** Botón elimina partido
- **Visual:** Muestra marcador destacado (ej: "3 - 1")

## Archivos Modificados/Creados

### Network Layer
- `ApiService.kt` - Endpoints POST/PUT/DELETE agregados para 4 entidades

### Repository Layer
- `JugadorRepository.kt` - createJugador, updateJugador, deleteJugador
- `EquipoRepository.kt` - createEquipo, updateEquipo, deleteEquipo
- `RivalRepository.kt` - createRival, updateRival, deleteRival
- `PartidoRepository.kt` - createPartido, updatePartido, deletePartido

### ViewModel Layer
- `JugadorViewModel.kt` - Acciones CRUD expuestas
- `EquipoViewModel.kt` - Acciones CRUD expuestas
- `RivalViewModel.kt` - Acciones CRUD expuestas
- `PartidoViewModel.kt` - Acciones CRUD expuestas

### UI Layer (Screens)
- `RivalListScreen.kt` - CRUD para rivales con diálogo
- `PlayerListScreen.kt` - CRUD para jugadores con formulario
- `TeamListScreen.kt` - CRUD para equipos con formulario
- `MatchListScreen.kt` - **NUEVA** CRUD para partidos

### Tests
- `RivalRepositoryTest.kt` - Tests unitarios para CRUD de rivales
- `PartidoRepositoryTest.kt` - Tests unitarios para CRUD de partidos

## Cómo Probar CRUD (Para la Defensa)

### En la App:
1. Abrir **RivalListScreen** → Presionar FAB "+" → Crear rival
2. Ver rival creado en la lista → Presionar ícono de lápiz → Editar nombre
3. Presionar ícono de basura → Eliminar rival
4. Repetir proceso en **PlayerListScreen**, **TeamListScreen**, **MatchListScreen**

### Con Tests Unitarios:
```bash
# Ejecutar tests de Rival
./gradlew test --tests RivalRepositoryTest

# Ejecutar tests de Partido
./gradlew test --tests PartidoRepositoryTest
```

### Con curl (Desde terminal, probando API en Render):
```bash
# Crear rival
curl -X POST https://ms-rivales.onrender.com/rivales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Equipo Test"}'

# Listar rivales
curl https://ms-rivales.onrender.com/rivales

# Actualizar rival
curl -X PUT https://ms-rivales.onrender.com/rivales/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"nombre":"Equipo Actualizado"}'

# Eliminar rival
curl -X DELETE https://ms-rivales.onrender.com/rivales/1
```

## Pasos para Ejecutar
1. Clonar el repositorio.
2. Abrir en Android Studio.
3. Sincronizar Gradle.
4. Ejecutar en Emulador o Dispositivo físico.
5. **Nota:** Esperar 60 segundos en la primera carga (Cold Start de Render).

## Stack Tecnológico
- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose
- **Networking:** Retrofit + OkHttp
- **Async:** Coroutines + Flow
- **Architecture:** MVVM + Clean Architecture
- **Serialization:** Gson
- **Testing:** JUnit + Mockito
- **Build Tool:** Gradle

## Notas para el Docente
✅ **CRUD Completo:** GET, POST (crear), PUT (actualizar), DELETE  
✅ **4 Entidades:** Partidos, Jugadores, Equipos, Rivales  
✅ **API REST:** Conectado a 4 microservicios en Render  
✅ **Manejo de Errores:** Result<T> con onSuccess/onFailure  
✅ **UI Funcional:** Diálogos para formularios, botones para acciones  
✅ **Tests:** Tests unitarios mostrando que métodos funcionan  
✅ **Validación:** Campos requeridos antes de enviar  
✅ **Feedback Visual:** Loading, errores y mensajes de éxito



