package fi.mazhar.dhikr.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import fi.mazhar.dhikr.android.data.AppHolder
import fi.mazhar.dhikr.ui.DhikrApp
import fi.mazhar.dhikr.ui.LocalAppPlatform
import fi.mazhar.dhikr.ui.Screen
import fi.mazhar.dhikr.ui.isDarkTheme

/** Scrim for light-theme navigation bars on API < 26, which cannot draw dark icons. */
private val LegacyNavScrim = Color.argb(0x80, 0x1B, 0x1B, 0x1B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = AppHolder.model(applicationContext)
        val platform = AndroidPlatform(this)
        setContent {
            // Transparent system bars so the app's own background (pure black on
            // OLED) shows behind them; icon colors follow the in-app theme choice.
            val dark = isDarkTheme(model.state.settings)
            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = if (dark) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                    },
                    navigationBarStyle = if (dark) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(Color.TRANSPARENT, LegacyNavScrim)
                    },
                )
                onDispose {}
            }
            CompositionLocalProvider(LocalAppPlatform provides platform) {
                BackHandler(enabled = model.screen !is Screen.Home || model.canGoBack()) {
                    model.back()
                }
                DhikrApp(model)
            }
        }
    }
}
