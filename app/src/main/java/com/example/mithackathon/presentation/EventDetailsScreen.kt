package com.example.mithackathon.presentation

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EventDetailsScreen(
    viewModel: EventViewModel,
    eventId: String
) {
    val currentEvent by viewModel.currentEvent.collectAsState()
    val attendees by viewModel.attendees.collectAsState()
    val qrCodeBitmap by viewModel.qrCodeBitmap.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val registrations by viewModel.registrations.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("attendees") }

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
        viewModel.generateQRCode(eventId)
        viewModel.loadEventRegistrations(eventId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Text(
            text = error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    } else if (currentEvent != null) {
        val event = currentEvent!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = event.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Club: ${event.clubName}")
                Text(text = "Type: ${event.type}")
                Text(text = "Date: ${event.date}")
                Text(text = "Start: ${event.startDate}")
                Text(text = "End: ${event.endDate}")
                Text(text = "Registration: ${event.registrationOpen} to ${event.registrationClose}")
                Text(text = "Location: ${event.location}")
                Text(text = "Fee: ₹${event.registrationFee}")
                Text(text = "Tag: ${event.tag}")
                Text(text = event.description)

                Divider()

                Text("QR Code to Mark Attendance", style = MaterialTheme.typography.titleMedium)
                qrCodeBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code for event attendance",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                            .border(1.dp, MaterialTheme.colorScheme.outline)
                    )
                }

                Divider()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        viewModel.deleteEvent(eventId)
                    }) {
                        Text("Delete Event")
                    }

                    Button(onClick = {
                        val shareText =
                            "Join the event \"${event.name}\" here: ${event.registrationLink}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Event"))
                    }) {
                        Text("Share Event")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { selectedTab = "attendees" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == "attendees") Color(0xFF1A237E) else Color.LightGray
                        )
                    ) {
                        Text("View Attendees", color = Color.White)
                    }

                    Button(
                        onClick = { selectedTab = "registrations" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == "registrations") Color(0xFF1A237E) else Color.LightGray
                        )
                    ) {
                        Text("View Registrations", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                val listToShow = if (selectedTab == "attendees") attendees else registrations

                if (listToShow.isEmpty()) {
                    item {
                        Text("No $selectedTab yet", modifier = Modifier.padding(top = 8.dp))
                    }
                } else {
                    items(listToShow) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(user.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(user.email, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



//
//@Composable
//fun EventDetailsScreen(
//    viewModel: EventViewModel,
//    eventId: String
//) {
//    val currentEvent by viewModel.currentEvent.collectAsState()
//    val attendees by viewModel.attendees.collectAsState()
//    val qrCodeBitmap by viewModel.qrCodeBitmap.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//
//    LaunchedEffect(eventId) {
//        viewModel.loadEvent(eventId)
//        viewModel.generateQRCode(eventId)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        if (isLoading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else if (error != null) {
//            Text(
//                text = error ?: "",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        } else if (currentEvent != null) {
//            val event = currentEvent!!
//
//            Text(
//                text = event.name,
//                style = MaterialTheme.typography.headlineMedium
//            )
//
//            Text(
//                text = "Date: ${event.date}",
//                style = MaterialTheme.typography.bodyLarge
//            )
//
//            Text(
//                text = event.description,
//                style = MaterialTheme.typography.bodyMedium
//            )
//
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//            Text(
//                text = "Scan QR Code to Mark Attendance",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            qrCodeBitmap?.let { bitmap ->
//                Image(
//                    bitmap = bitmap.asImageBitmap(),
//                    contentDescription = "QR Code for event attendance",
//                    modifier = Modifier
//                        .size(200.dp)
//                        .align(Alignment.CenterHorizontally)
//                        .border(1.dp, MaterialTheme.colorScheme.outline)
//                )
//            }
//
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//            Text(
//                text = "Attendees (${attendees.size})",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            LazyColumn {
//                items(attendees) { user ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = null
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Column {
//                                Text(
//                                    text = user.name,
//                                    style = MaterialTheme.typography.bodyLarge
//                                )
//                                Text(
//                                    text = user.email,
//                                    style = MaterialTheme.typography.bodyMedium
//                                )
//                            }
//                        }
//                    }
//                }
//
//                if (attendees.isEmpty()) {
//                    item {
//                        Text(
//                            text = "No attendees yet",
//                            style = MaterialTheme.typography.bodyMedium,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}