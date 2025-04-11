package com.example.mithackathon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mithackathon.data.Repositories.FirebaseRepository
import com.example.mithackathon.presentation.AttendanceViewModel
import com.example.mithackathon.presentation.AuthScreen
import com.example.mithackathon.presentation.CreateEventScreen
import com.example.mithackathon.presentation.EventDetailsScreen
import com.example.mithackathon.presentation.EventViewModel
import com.example.mithackathon.presentation.EventsListScreen
import com.example.mithackathon.presentation.QRScannerScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(navController = navController , startDestination = AppScreens.AuthScreen.name){
        composable(AppScreens.AuthScreen.name) {
            AuthScreen(navController = navController)
        }
        composable(AppScreens.HomeScreen.name) {
            val repository = remember { FirebaseRepository() }
            val eventViewModel = viewModel { EventViewModel(repository) }

            EventsListScreen(
                viewModel = eventViewModel,
                navController = navController,
                onEventClick = { eventId ->
                    navController.navigate("${AppScreens.EventDetailsScreen.name}/$eventId")
                }
            )
        }
        composable("${AppScreens.EventDetailsScreen.name}/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            val repository = remember { FirebaseRepository() }
            val eventViewModel = viewModel { EventViewModel(repository) }

            EventDetailsScreen(viewModel = eventViewModel , eventId)
        }
        composable(AppScreens.ScanQrScreen.name) {
            val repository = remember { FirebaseRepository() }
            val attendanceViewModel = viewModel { AttendanceViewModel(repository) }

            QRScannerScreen(
                viewModel = attendanceViewModel,
                onScanComplete = {
                    // Navigate to Events List or any other screen on scan complete
                    navController.navigate(AppScreens.HomeScreen.name) {
                        popUpTo(AppScreens.ScanQrScreen.name) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreens.CreateEventScreen.name) {
            val repository = remember { FirebaseRepository() }
            val viewModel = viewModel { EventViewModel(repository) }

            CreateEventScreen(
                viewModel = viewModel,
                onEventCreated = {
                    navController.navigate(AppScreens.HomeScreen.name) {
                        popUpTo(AppScreens.CreateEventScreen.name) { inclusive = true }
                    }
                }
            )
        }

    }
}

//@Composable
//fun AttendanceNavigation(navController: NavHostController) {
//    val navController = rememberNavController()
//    val repository = remember { FirebaseRepository() }
//    val eventViewModel = viewModel { EventViewModel(repository) }
//    val attendanceViewModel = viewModel { AttendanceViewModel(repository) }
//
//    NavHost(navController = navController, startDestination = "events") {
//        composable("events") {
//            EventsListScreen(
//                viewModel = eventViewModel,
//                onCreateEvent = { navController.navigate("create_event") },
//                onEventClick = { eventId -> navController.navigate("event_details/$eventId") }
//            )
//        }
//
//        composable("create_event") {
//            CreateEventScreen(
//                viewModel = eventViewModel,
//                onEventCreated = {
//                    eventViewModel.currentEvent.value?.id?.let { eventId ->
//                        navController.navigate("event_details/$eventId") {
//                            popUpTo("events")
//                        }
//                    }
//                }
//            )
//        }
//
//        composable(
//            route = "event_details/{eventId}",
//            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
//            EventDetailsScreen(
//                viewModel = eventViewModel,
//                eventId = eventId
//            )
//        }
//
//        composable("scan_qr") {
//            QRScannerScreen(
//                viewModel = attendanceViewModel,
//                onScanComplete = { navController.popBackStack("events", false) }
//            )
//        }
//    }
//}

