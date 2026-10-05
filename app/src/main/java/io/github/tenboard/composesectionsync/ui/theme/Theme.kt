package io.github.tenboard.composesectionsync.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF42672E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC4EFA6),
    onPrimaryContainer = Color(0xFF102006),
    background = Color(0xFFF9FAF4),
    onBackground = Color(0xFF1A1D17),
    surface = Color(0xFFF9FAF4),
    onSurface = Color(0xFF1A1D17),
    surfaceContainer = Color(0xFFEDF1E5),
    surfaceContainerHighest = Color(0xFFE1E7D8),
    onSurfaceVariant = Color(0xFF454D3E),
    outlineVariant = Color(0xFFC5CDBB),
    surfaceTint = Color(0xFF42672E),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA8D28C),
    onPrimary = Color(0xFF173807),
    primaryContainer = Color(0xFF2D4F1C),
    onPrimaryContainer = Color(0xFFC4EFA6),
    background = Color(0xFF12150F),
    onBackground = Color(0xFFE1E5D9),
    surface = Color(0xFF12150F),
    onSurface = Color(0xFFE1E5D9),
    surfaceContainer = Color(0xFF20251C),
    surfaceContainerHighest = Color(0xFF30372A),
    onSurfaceVariant = Color(0xFFC3CCB7),
    outlineVariant = Color(0xFF454D3E),
    surfaceTint = Color(0xFFA8D28C),
)

@Composable
fun ComposeSectionSyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}
