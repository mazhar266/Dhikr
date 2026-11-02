package fi.mazhar.dhikr.ui

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
import fi.mazhar.dhikr.AppTheme
import fi.mazhar.dhikr.Settings

// Soft ivory with muted emerald and restrained gold accents.
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
    surfaceDim = Color(0xFFE3DFD2),
    surfaceBright = Color(0xFFFFFCF4),
    surfaceContainerLowest = Color(0xFFFFFEF9),
    surfaceContainerLow = Color(0xFFF7F4EA),
    surfaceContainer = Color(0xFFF3F0E5),
    surfaceContainerHigh = Color(0xFFEFECE0),
    surfaceContainerHighest = Color(0xFFECE8DB),
    inverseSurface = Color(0xFF2F3735),
    inverseOnSurface = Color(0xFFF1EEE6),
    inversePrimary = Color(0xFF8FD2C0),
    outline = Color(0xFFB9B8AB),
    outlineVariant = Color(0xFFD9D6CA),
    error = Color(0xFFB0574A),
)

// Warm parchment light theme.
private val SofiaPaperColors = lightColorScheme(
    primary = Color(0xFF33685A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE8DA),
    onPrimaryContainer = Color(0xFF1E352C),
    secondary = Color(0xFF9C7A35),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEFE3C6),
    onSecondaryContainer = Color(0xFF3D3115),
    background = Color(0xFFF4EDDE),
    onBackground = Color(0xFF322D22),
    surface = Color(0xFFFAF5E9),
    onSurface = Color(0xFF322D22),
    surfaceVariant = Color(0xFFECE3CF),
    onSurfaceVariant = Color(0xFF6A6353),
    surfaceDim = Color(0xFFE3DAC6),
    surfaceBright = Color(0xFFFAF5E9),
    surfaceContainerLowest = Color(0xFFFFFBF3),
    surfaceContainerLow = Color(0xFFF7F1E4),
    surfaceContainer = Color(0xFFF2EBDB),
    surfaceContainerHigh = Color(0xFFEFE7D5),
    surfaceContainerHighest = Color(0xFFECE3CF),
    inverseSurface = Color(0xFF37322A),
    inverseOnSurface = Color(0xFFF7F0E3),
    inversePrimary = Color(0xFF9DD0BE),
    outline = Color(0xFFC3BAA3),
    outlineVariant = Color(0xFFD9D0BB),
    error = Color(0xFFA94D3F),
)

// Deep navy dark theme.
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
    surfaceDim = Color(0xFF101720),
    surfaceBright = Color(0xFF2A3743),
    surfaceContainerLowest = Color(0xFF0B1118),
    surfaceContainerLow = Color(0xFF131B24),
    surfaceContainer = Color(0xFF161F29),
    surfaceContainerHigh = Color(0xFF1A2530),
    surfaceContainerHighest = Color(0xFF1E2A36),
    inverseSurface = Color(0xFFDEE5E2),
    inverseOnSurface = Color(0xFF1E2A36),
    inversePrimary = Color(0xFF2E7D6C),
    outline = Color(0xFF4A5653),
    outlineVariant = Color(0xFF2E3A44),
    error = Color(0xFFE29A8E),
)

// True-black dark theme for OLED panels (no gray glow, slightly brighter accents).
// Every surface and bar is #000; only cards and dialogs lift to a near-black
// neutral so they stay distinguishable.
private val OledColors = darkColorScheme(
    primary = Color(0xFF52B79E),
    onPrimary = Color(0xFF00201A),
    primaryContainer = Color(0xFF123A31),
    onPrimaryContainer = Color(0xFFBFE8DC),
    secondary = Color(0xFFD8BC78),
    onSecondary = Color(0xFF241B03),
    secondaryContainer = Color(0xFF2E2610),
    onSecondaryContainer = Color(0xFFEEDDAF),
    background = Color(0xFF000000),
    onBackground = Color(0xFFD6DDDA),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFD6DDDA),
    surfaceVariant = Color(0xFF141414),
    onSurfaceVariant = Color(0xFFA8B2AE),
    surfaceDim = Color(0xFF000000),
    surfaceBright = Color(0xFF1C1C1C),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = Color(0xFF000000),
    surfaceContainer = Color(0xFF000000),
    surfaceContainerHigh = Color(0xFF101010),
    surfaceContainerHighest = Color(0xFF141414),
    inverseSurface = Color(0xFFD6DDDA),
    inverseOnSurface = Color(0xFF141414),
    inversePrimary = Color(0xFF2E7D6C),
    outline = Color(0xFF333333),
    outlineVariant = Color(0xFF262626),
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

/** Whether [settings] resolve to a dark palette; hosts use it for system bar icon colors. */
@Composable
fun isDarkTheme(settings: Settings): Boolean = when (settings.theme) {
    AppTheme.LIGHT, AppTheme.SOFIA_PAPER -> false
    AppTheme.DARK, AppTheme.OLED -> true
    AppTheme.SYSTEM -> isSystemInDarkTheme()
}

@Composable
fun DhikrAppTheme(settings: Settings, content: @Composable () -> Unit) {
    val colorScheme = when (settings.theme) {
        AppTheme.LIGHT -> LightColors
        AppTheme.SOFIA_PAPER -> SofiaPaperColors
        AppTheme.DARK -> DarkColors
        AppTheme.OLED -> OledColors
        AppTheme.SYSTEM -> if (isSystemInDarkTheme()) DarkColors else LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
