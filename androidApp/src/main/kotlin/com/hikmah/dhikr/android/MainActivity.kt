package com.hikmah.dhikr.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hikmah.dhikr.android.data.AppHolder
import com.hikmah.dhikr.android.ui.DhikrApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = AppHolder.model(applicationContext)
        setContent {
            DhikrApp(model)
        }
    }
}
