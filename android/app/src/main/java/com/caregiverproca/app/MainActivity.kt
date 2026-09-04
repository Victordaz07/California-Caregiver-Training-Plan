package com.caregiverproca.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.caregiverproca.app.ui.navigation.CaregiverNavHost
import com.caregiverproca.app.ui.theme.CaregiverProCATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaregiverProCATheme {
                CaregiverNavHost()
            }
        }
    }
}
