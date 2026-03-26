package com.betha.medicapp.ui.theme

import androidx.compose.ui.graphics.Color

// Colores principales
val Primary = Color(0xFF1565C0)        // Azul principal
val PrimaryLight = Color(0xFF5E92F3)    // Azul claro
val PrimaryDark = Color(0xFF003C8F)     // Azul oscuro

// Fondos - blancos y pasteles
val Background = Color(0xFFFAFAFA)      // Blanco casi puro
val Surface = Color(0xFFFFFFFF)         // Blanco
val SurfaceVariant = Color(0xFFF5F7FA)  // Azul muy pastel (gris azulado)

// Texto
val OnPrimary = Color(0xFFFFFFFF)       // Texto sobre azul
val OnBackground = Color(0xFF1A1A1A)    // Negro suave
val OnSurface = Color(0xFF333333)       // Gris oscuro
val TextSecondary = Color(0xFF757575)   // Gris medio

// Estados
val Error = Color(0xFFD32F2F)
val Success = Color(0xFF388E3C)

// Alias para compatibilidad con código existente
val BlueEssenza = Primary
val BlueEssenzaLight = PrimaryLight
val BlueEssenzaDark = PrimaryDark
val BlueGradientStart = PrimaryDark
val BlueGradientEnd = Primary
val GreenEssenza = Primary
val GreenEssenzaLight = PrimaryLight
val GreenEssenzaDark = PrimaryDark
val GreenGradientStart = PrimaryDark
val GreenGradientEnd = Primary

val White = Surface
val LightGray = SurfaceVariant
val DarkGray = OnSurface
val ErrorRed = Error
