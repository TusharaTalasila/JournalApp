package com.example.journalapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
//currently use same colors for both
private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Black,
    secondary = BrightGreen,
    onSecondary = Black,
    tertiary = Mint,
    onTertiary = Black,
    background = Cream,
    onBackground = Black,
    surface = ScrollBg,
    onSurface = Black,
   // error = RedError, // Define RedError or use a default error color
    onError = Black,
    outline = LinkGreen,
    onPrimaryContainer = buttonText

)

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = Black,
    secondary = BrightGreen,
    onSecondary = Black,
    tertiary = Mint,
    onTertiary = Black,
    background = Cream,
    onBackground = Black,
    surface = ScrollBg,
    onSurface = Black,
   // error = RedError, // Define RedError or use a default error color
    onError = Black,
    outline = LinkGreen,
    onPrimaryContainer = buttonText
)


@Composable
fun JournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
val MaterialTheme.spacing: Spacing
    get() = Spacing()
