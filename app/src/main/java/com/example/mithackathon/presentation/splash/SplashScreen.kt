package com.example.mithackathon.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mithackathon.InternalFun.getUserInfoFromPrefs
import com.example.mithackathon.navigation.AppScreens
import com.example.mithackathon.presentation.Auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(1500) // Optional: splash delay for UX

        val user = auth.currentUser
        if (user == null) {
            // Not logged in
            navController.navigate(AppScreens.SignInScreen.name) {
                popUpTo(AppScreens.SplashScreen.name) { inclusive = true }
            }
        } else {
            val (_, _, type) = getUserInfoFromPrefs(context)
            when (type) {
                "Student" -> navController.navigate(AppScreens.MainScreen.name) {
                    popUpTo(AppScreens.SplashScreen.name) { inclusive = true }
                }
                "Club" -> navController.navigate(AppScreens.ClubDashBoardScreen.name) {
                    popUpTo(AppScreens.SplashScreen.name) { inclusive = true }
                }
                else -> {
                    // No type stored, fallback to sign in
                    navController.navigate(AppScreens.SignInScreen.name) {
                        popUpTo(AppScreens.SplashScreen.name) { inclusive = true }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your App Name",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5)
        )
    }
}
