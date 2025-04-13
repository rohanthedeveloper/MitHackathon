package com.example.mithackathon.presentation

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(text = event.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Club: ${event.clubName}")
                Text(text = "Type: ${event.tag}")
                Text(text = "Date: ${event.date}")
                Text(text = "Start: ${event.registrationOpen}")
                Text(text = "End: ${event.registrationClose}")
                Text(text = "Registration: ${event.registrationOpen} to ${event.registrationClose}")
                Text(text = "Location: ${event.location}")
                Text(text = "Fee: ₹${event.registrationFee}")
                Text(text = event.description)

                Divider()
                Box(Modifier.fillMaxWidth()) {
                    Column(Modifier .align(Alignment.Center)) {
                        Text("QR Code to Mark Attendance", style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier)
                        qrCodeBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code for event attendance",
                                modifier = Modifier
                                    .size(200.dp)

                                    .border(1.dp, MaterialTheme.colorScheme.outline)
                            )
                    }

                    }
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

                Spacer(modifier = Modifier.height(8.dp))
            }

            val listToShow = if (selectedTab == "attendees") attendees else registrations
            Log.d("test", listToShow.toList().toString())

            if (listToShow.isNotEmpty()) {
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
            } else {
                item {
                    Text(
                        text = "No $selectedTab yet",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
