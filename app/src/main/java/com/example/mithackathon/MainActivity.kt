package com.example.mithackathon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mithackathon.data.Repositories.FirebaseRepository
import com.example.mithackathon.navigation.AppNavigation
import com.example.mithackathon.navigation.AppScreens
import com.example.mithackathon.presentation.AttendanceViewModel
import com.example.mithackathon.ui.theme.MitHackathonTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {

    private val LocalNavController = compositionLocalOf<NavController> {
        error("No NavController provided")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle deep links for QR code scanning
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith("attendanceapp://event/")) {
            val eventId = uri.lastPathSegment
            if (eventId != null) {
                val repository = FirebaseRepository()
                val viewModel = AttendanceViewModel(repository)
                viewModel.markAttendance(eventId)
            }
        }

        setContent {
            MitHackathonTheme {
                val navController = rememberNavController()

                CompositionLocalProvider(LocalNavController provides navController) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}