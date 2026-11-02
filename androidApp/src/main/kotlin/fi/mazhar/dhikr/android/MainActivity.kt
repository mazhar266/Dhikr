package fi.mazhar.dhikr.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fi.mazhar.dhikr.android.data.AppHolder
import fi.mazhar.dhikr.android.ui.DhikrApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = AppHolder.model(applicationContext)
        setContent {
            DhikrApp(model)
        }
    }
}
