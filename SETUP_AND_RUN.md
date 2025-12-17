# 🚀 SETUP & RUN - Cómo Ejecutar la App

## ✅ Requisitos Previos

- **Android Studio:** Descargar desde https://developer.android.com/studio
- **JDK 11+:** Incluido en Android Studio
- **Emulador Android:** Pixel 4a API 33 o superior (recomendado)
- **Espacio en disco:** 5 GB mínimo
- **Conexión a Internet:** Requerida (para microservicios en Render)

---

## 📋 Paso 1: Preparar el Proyecto

### 1.1 Clonar/Abrir el Repositorio
```bash
# Si está en GitHub
git clone https://github.com/tuusuario/AppFutbol.git

# O simplemente abrir la carpeta en Android Studio
File → Open → Seleccionar C:\Users\SSDD\StudioProjects\AppFutbol
```

### 1.2 Esperar Sincronización de Gradle
```
Android Studio mostrará:
"Gradle: sync in progress..."

Esperar hasta que diga:
✅ "Gradle sync finished successfully"

(Puede tomar 2-5 minutos la primera vez)
```

### 1.3 Verificar que no hay errores
```
En la parte inferior:
❌ Error → Click en "Make Project" para recompilar
✅ Sin errores → Listo para correr
```

---

## 📱 Paso 2: Configurar Emulador

### Opción A: Usar Emulador Existente
```
1. Abrir Android Studio
2. Arriba a la derecha: AVD Manager (teléfono icon)
3. Ver lista de emuladores
4. Si hay uno: Click en ▶️ (Play) para iniciar
5. Esperar que arranque (~30 segundos)
```

### Opción B: Crear Emulador Nuevo
```
1. Android Studio → Tools → Device Manager
2. Click en "+ Create Device"
3. Seleccionar "Pixel 4a"
4. Click Next
5. Seleccionar "API Level 33" (o mayor)
6. Click Next → Finish
7. Click ▶️ para iniciar
```

### Opción C: Usar Dispositivo Físico
```
1. Conectar teléfono Android con cable USB
2. En el teléfono: Ir a Ajustes → Información del Dispositivo
3. Tocar "Número de compilación" 7 veces
4. Volver a Ajustes → Opciones de Desarrollo
5. Activar "Depuración USB"
6. En Android Studio: Device selector mostrará tu teléfono
```

---

## ▶️ Paso 3: Ejecutar la App

### 3.1 Seleccionar Device
```
En Android Studio (arriba a la derecha):
[Pixel 4a API 33] ← Click aquí para seleccionar device
```

### 3.2 Ejecutar App
```
Opción A (Click):
- Click verde ▶️ (Run Button) arriba
- O presionar Shift + F10

Opción B (Menú):
- Run → Run 'app'

Opción C (Teclado):
- Ctrl + R (Windows/Linux)
- Cmd + R (Mac)
```

### 3.3 Seleccionar Configuración
```
Si pregunta "Select Deployment Target":
- Seleccionar emulador o dispositivo
- Check "Use the same selection for future launches"
- Click OK
```

### 3.4 Esperar Compilación
```
Android Studio compilará:
- Kotlin ✓
- Resources ✓
- APK ✓

Entonces instalará en el device:
- "Installing APK" 
- "Launching activity"

Tiempo total: 1-3 minutos (primera vez: hasta 5 min)
```

---

## 🎬 Paso 4: Ver la App Ejecutándose

```
Emulador/Teléfono mostrará:
┌─────────────────────────────┐
│         AppFutbol           │
│                             │
│  [Login Screen]             │
│  User: ___________          │
│  Pass: ___________          │
│                             │
│     [Login]  [Register]     │
└─────────────────────────────┘

Para testear CRUD:
1. Login (si es necesario)
2. Ir a Home
3. Click en "Rivales" o "Jugadores"
4. Ver pantalla CRUD
5. Tocar FAB (+) para crear
```

---

## 🧪 Paso 5: Ejecutar Tests

### 5.1 Tests Unitarios
```
Android Studio:
1. Click derecho en: app/src/test/java/com/example/uinavegacion/
2. Seleccionar: Run Tests
3. Ver console output:
   ✅ RivalRepositoryTest: 5/5 tests passed
   ✅ PartidoRepositoryTest: 5/5 tests passed
```

### 5.2 Tests Específicos
```
Abrir archivo:
- RivalRepositoryTest.kt
- Click derecho
- Run 'RivalRepositoryTest'

Ver console:
BUILD SUCCESSFUL
5 tests passed
```

### 5.3 Tests con Cobertura
```
Click derecho en RivalRepositoryTest.kt
→ Run 'RivalRepositoryTest' with Coverage
→ Ver coverage report
```

---

## 🔧 Troubleshooting

### Problema 1: Gradle Sync Falla
```
Solución:
1. File → Invalidate Caches
2. Seleccionar "Invalidate and Restart"
3. Esperar reinicio
4. Gradle sync debería funcionar
```

### Problema 2: APK Installation Failed
```
Solución:
1. Emulador: Ir a Settings → Apps → AppFutbol → Uninstall
2. Volver a Android Studio
3. Click ▶️ nuevamente
```

### Problema 3: Emulador muy lento
```
Solución:
1. Cerrar otras apps (Chrome, etc.)
2. AVD Manager → Edit config del emulador
3. Aumentar RAM a 4GB
4. Reiniciar emulador
```

### Problema 4: Microservicios en Render tardan
```
Solución:
Esto es normal. El primer request tardará ~60 segundos.
Esperar pacientemente. Luego será rápido.
```

### Problema 5: No ve cambios en código
```
Solución:
1. Build → Clean Project
2. Build → Rebuild Project
3. Run ▶️ nuevamente
```

---

## 📊 Verificación Final

```
Checklist antes de defender:
☑ App compila sin errores
☑ App corre en emulador/device
☑ Pantalla de Rivales carga
☑ FAB (+) abre diálogo
☑ Puede crear rival
☑ Rival aparece en lista
☑ Puede editar rival
☑ Puede eliminar rival
☑ Tests unitarios pasan
☑ Android Studio está abierta
```

---

## 💻 Comandos Útiles (Terminal)

### Compilar APK
```bash
cd C:\Users\SSDD\StudioProjects\AppFutbol
.\gradlew assembleDebug
```

### Ejecutar Tests
```bash
.\gradlew test --tests RivalRepositoryTest
.\gradlew test --tests PartidoRepositoryTest
```

### Limpiar Build
```bash
.\gradlew clean
```

### Ver APK generado
```
Ubicación: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🎯 Línea de Tiempo (Primero uso)

| Paso | Tiempo | Qué |
|------|--------|-----|
| 1. Abrir proyecto | 1 min | File → Open AppFutbol |
| 2. Gradle sync | 3-5 min | Esperar "Sync finished" |
| 3. Crear emulador | 5 min | AVD Manager → Create |
| 4. Iniciar emulador | 2 min | Click ▶️ |
| 5. Compilar app | 2 min | Click ▶️ Run |
| 6. Instalar APK | 1 min | Automático |
| 7. Ver app correr | - | App abre en emulador |
| **TOTAL** | **14-16 min** | **Primera vez** |

**Siguientes veces:** Solo 1-2 minutos (emulador ya está abierto)

---

## 🚀 Listo!

```
Si todo está correcto verás:

Android Studio Console:
✅ BUILD SUCCESSFUL in 2m 3s

Emulador:
✅ App abierta mostrando Login o Home

Ready para demo:
✅ Presiona en Rivales
✅ Presiona FAB (+)
✅ Crea, edita, elimina
✅ ¡ Impresiona al docente ! 🎉
```

---

## 📞 Soporte Rápido

Si algo no funciona:
1. Abrir READING_GUIDE.md
2. Buscar el problema en "Troubleshooting"
3. Aplicar solución
4. Reintentar

Si aún no funciona:
1. Limpiar build: `./gradlew clean`
2. Invalidar caches: File → Invalidate Caches
3. Reinstalar app en emulador
4. Buscar error en console

---

**¡ Ya está todo listo para correr ! 🎬**

Presiona ▶️ Run y ¡ a defender ! 🚀

