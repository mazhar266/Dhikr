package fi.mazhar.dhikr.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import fi.mazhar.dhikr.android.data.AppHolder
import fi.mazhar.dhikr.ui.DhikrApp
import fi.mazhar.dhikr.ui.LocalAppPlatform
import fi.mazhar.dhikr.ui.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = AppHolder.model(applicationContext)
        val platform = AndroidPlatform(this)
        setContent {
            CompositionLocalProvider(LocalAppPlatform provides platform) {
                BackHandler(enabled = model.screen !is Screen.Home || model.canGoBack()) {
                    model.back()
                }
                DhikrApp(model)
            }
        }
    }
}
