package com.example.magnapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.magnapp.ui.Navigation
import com.example.magnapp.ui.theme.MagnAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // TEST
            val prueba = "hola prueba desde dell"
            MagnAppTheme {
                Navigation()
            }
        }
    }
}