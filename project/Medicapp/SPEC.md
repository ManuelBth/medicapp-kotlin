# SPEC.md - PatientActivity y DoctorActivity

## 1. Project Overview

**Project**: MedicApp (Android Kotlin)  
**Feature**: PatientActivity y DoctorActivity - Pantallas post-login para pacientes y doctores  
**Target Users**: Pacientes y doctores de la aplicación médica

## 2. Architecture

### 2.1 Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose con Material3
- **Navigation**: Compose Navigation (AppNavigation existente)
- **Networking**: RESTClient existente
- **Local Storage**: SharedPreferences (SessionManager)

### 2.2 Module Structure
```
app/src/main/java/com/betha/medicapp/
├── auth/
│   └── service/
│       ├── AppointmentService.kt     (NEW - API calls)
│       └── SessionManager.kt         (NEW - SharedPreferences)
├── auth/
│   └── presentation/
│       └── dto/
│           ├── Doctor.kt             (NEW - modelo doctor)
│           ├── Appointment.kt        (NEW - modelo cita)
│           └── ScheduleRequest.kt     (NEW - DTO para agendar)
├── auth/
│   └── ui/
│       └── screens/
│           ├── PatientScreen.kt      (NEW)
│           └── DoctorScreen.kt       (NEW)
└── ui/
    └── navigation/
        └── Navigation.kt             (MODIFIED - agregar rutas)
```

## 3. API Specification

### 3.1 Obtener Lista de Doctores
- **Endpoint**: `GET /getdoctors?idNumber={id}`
- **Request**: Query param `idNumber` (Int)
- **Response**:
```json
[
  {"doctorName": "Juan"},
  {"doctorName": "Hector"}
]
```
- **Error Handling**: Mostrar Toast con error en caso de fallo

### 3.2 Programar Cita
- **Endpoint**: `POST /scheduleappointment`
- **Request Body**:
```json
{
  "patientId": "1",
  "doctorName": "Juan",
  "date": "25/03/2026",
  "time": "16:00"
}
```
- **Response**:
```json
{
  "status": "Appointment successfully scheduled"
}
```
- **Error Handling**: Mostrar Toast con respuesta del servidor

### 3.3 Obtener Citas Pendientes (Doctor)
- **Endpoint**: `GET /getappointments?idNumber={id}`
- **Request**: Query param `idNumber` (Int)
- **Response**:
```json
[
  {"patientName": "Daniel", "date": "25/03/2026", "time": "14:00"},
  {"patientName": "Manuel", "date": "25/03/2026", "time": "16:00"}
]
```
- **Error Handling**: Mostrar Toast con error en caso de fallo

## 4. UI/UX Specification

### 4.1 Shared Design
- **Background**: BlueGradientStart (#0D47A1) → BlueGradientEnd (#1976D2)
- **Typography**: Roboto, títulos en Bold
- **Spacing**: 16dp padding estándar, 8dp entre elementos
- **Componentes**: Material3 Compose

### 4.2 PatientScreen

#### Layout
```
┌─────────────────────────────┐
│    [Blue Gradient BG]        │
│                             │
│  "Seleccionar Doctor"        │
│  ┌─────────────────────┐    │
│  │ Dropdown/Spinner     │ ▼  │
│  └─────────────────────┘    │
│                             │
│  "Seleccionar Fecha"        │
│  ┌─────────────────────┐    │
│  │ DatePicker           │    │
│  └─────────────────────┘    │
│                             │
│  "Seleccionar Hora"         │
│  ┌─────────────────────┐    │
│  │ TimePicker           │    │
│  └─────────────────────┘    │
│                             │
│  ┌─────────────────────┐    │
│  │  Agendar Cita       │    │
│  └─────────────────────┘    │
│                             │
│  [Cerrar Sesión]            │
└─────────────────────────────┘
```

#### Comportamiento
1. Al cargar: hacer GET `/getdoctors?idNumber={id}` y poblar dropdown
2. Al seleccionar doctor: guardar selección en estado
3. Al abrir DatePicker: mostrar diálogo nativo
4. Al abrir TimePicker: mostrar diálogo nativo
5. Al presionar "Agendar":
   - Validar que doctor, fecha y hora estén seleccionados
   - POST `/scheduleappointment` con body
   - Mostrar Toast con respuesta
   - Limpiar selección

### 4.3 DoctorScreen

#### Layout
```
┌─────────────────────────────┐
│    [Blue Gradient BG]        │
│                             │
│  "Citas Pendientes"         │
│                             │
│  ┌─────────────────────┐    │
│  │ Daniel - 25/03 14:00│    │
│  ├─────────────────────┤    │
│  │ Manuel - 25/03 16:00│    │
│  └─────────────────────┘    │
│                             │
│  (Lista scrollable)         │
│                             │
│  [Cerrar Sesión]            │
└─────────────────────────────┘
```

#### Comportamiento
1. Al cargar: hacer GET `/getappointments?idNumber={id}`
2. Mostrar lista de citas (solo lectura)
3. Si no hay citas: mostrar mensaje "No hay citas pendientes"

## 5. Session Management

### 5.1 SessionManager
- **Archivo**: SharedPreferences `medicapp_session`
- **Keys**:
  - `user_id`: Int - ID del usuario logueado
  - `user_name`: String - Nombre del usuario
  - `is_doctor`: Boolean - Flag del tipo de usuario
- **Funciones**:
  - `saveSession(idNumber: Int, userName: String, isDoctor: Boolean)`
  - `getIdNumber(): Int?`
  - `getUserName(): String?`
  - `isDoctor(): Boolean`
  - `clearSession()`

## 6. Navigation Flow

### 6.1 Modificaciones en Navigation.kt
```kotlin
sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
    object Patient : Screen()    // NEW
    object Doctor : Screen()    // NEW
}
```

### 6.2 Flujo
```
LoginScreen → (éxito) → HomeScreen
                          │
                          ├─ doctor = false → PatientScreen
                          │
                          └─ doctor = true → DoctorScreen
```

### 6.3 Modificación en HomeScreen
- Agregar lógica para navegar a PatientScreen o DoctorScreen
- Eliminar contenido actual (solo mostrar bienvenido)
- O mantener como fallback y agregar botones de navegación

## 7. Technical Implementation

### 7.1 Dependencies
- No nuevas dependencias requeridas
- Usar librerías existentes: Compose, Material3, Coroutines

### 7.2 ViewModels
- **PatientViewModel**: manejar estado de doctores, selección, fecha/hora
- **DoctorViewModel**: manejar estado de citas

### 7.3 DTOs
```kotlin
data class Doctor(val doctorName: String)
data class Appointment(val patientName: String, val date: String, val time: String)
data class ScheduleRequest(
    val patientId: String,
    val doctorName: String,
    val date: String,
    val time: String
)
```

## 8. Acceptance Criteria

### 8.1 PatientScreen
- [ ] Carga lista de doctores al iniciar
- [ ] Dropdown muestra todos los doctores
- [ ] DatePicker permite seleccionar fecha
- [ ] TimePicker permite seleccionar hora
- [ ] Botón agendar envía request al servidor
- [ ] Toast muestra respuesta del servidor
- [ ] Navegación a login al cerrar sesión

### 8.2 DoctorScreen
- [ ] Carga citas pendientes al iniciar
- [ ] Muestra lista de citas con paciente, fecha, hora
- [ ] Mensaje cuando no hay citas
- [ ] Navegación a login al cerrar sesión

### 8.3 Shared
- [ ] Background con gradiente azul
- [ ] Material3 Components
- [ ] Sesión persistente via SharedPreferences
- [ ] Navegación correcta según flag `doctor`
