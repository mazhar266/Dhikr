package com.hikmah.dhikr.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hikmah.dhikr.AppTheme
import com.hikmah.dhikr.Settings

// Soft ivory / deep navy with muted emerald and restrained gold accents.
private val LightColors = lightColorScheme(
    primary = Color(0xFF2E7D6C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD8EEE7),
    onPrimaryContainer = Color(0xFF17352D),
    secondary = Color(0xFFA5813F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF2E6CB),
    onSecondaryContainer = Color(0xFF3E3113),
    background = Color(0xFFFAF7EF),
    onBackground = Color(0xFF26312F),
    surface = Color(0xFFFFFCF4),
    onSurface = Color(0xFF26312F),
    surfaceVariant = Color(0xFFECE8DB),
    onSurfaceVariant = Color(0xFF4C5551),
    outline = Color(0xFFB9B8AB),
    error = Color(0xFFB0574A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF5FB2A0),
    onPrimary = Color(0xFF0B2620),
    primaryContainer = Color(0xFF1E4A40),
    onPrimaryContainer = Color(0xFFC6EBDF),
    secondary = Color(0xFFD3B672),
    onSecondary = Color(0xFF2C2105),
    secondaryContainer = Color(0xFF41371B),
    onSecondaryContainer = Color(0xFFF0DFB4),
    background = Color(0xFF101720),
    onBackground = Color(0xFFDEE5E2),
    surface = Color(0xFF161F29),
    onSurface = Color(0xFFDEE5E2),
    surfaceVariant = Color(0xFF1E2A36),
    onSurfaceVariant = Color(0xFFB3BFBA),
    outline = Color(0xFF4A5653),
    error = Color(0xFFE29A8E),
)

private val AppTypography = Typography().run {
    copy(
        headlineMedium = headlineMedium.copy(fontWeight = FontWeight.SemiBold),
        titleLarge = titleLarge.copy(fontWeight = FontWeight.SemiBold),
        labelSmall = labelSmall.copy(letterSpacing = 0.4.sp),
    )
}

/** Large, readable Arabic style; [scale] comes from Settings (0.8–1.6). */
@Composable
fun arabicTextStyle(scale: Float): TextStyle = TextStyle(
    fontSize = (26 * scale).sp,
    lineHeight = (46 * scale).sp,
    fontWeight = FontWeight.Medium,
)

@Composable
fun DhikrAppTheme(settings: Settings, content: @Composable () -> Unit) {
    val dark = when (settings.theme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}
