package com.example.mithackathon.presentation

import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.util.Calendar
import android.widget.DatePicker
import android.app.DatePickerDialog
import androidx.compose.ui.graphics.asImageBitmap



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable


import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun CreateEventScreen(
    viewModel: EventViewModel,
    onEventCreated: () -> Unit
) {
    var eventTitle by rememberSaveable { mutableStateOf("") }
    var eventDescription by rememberSaveable { mutableStateOf("") }
    var eventDate by rememberSaveable { mutableStateOf("") }
    var openDate by rememberSaveable { mutableStateOf("") }
    var closeDate by rememberSaveable { mutableStateOf("") }
    var selectedTag by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var registrationLink by rememberSaveable { mutableStateOf("") }
    var registrationFee by rememberSaveable { mutableStateOf("") }
    var posterUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentEvent by viewModel.currentEvent.collectAsState()
    val qrCodeBitmap by viewModel.qrCodeBitmap.collectAsState()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    LaunchedEffect(currentEvent) {
        if (currentEvent != null) {
            onEventCreated()
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        posterUri = uri
    }

    val eventDatePickerDialog = remember {
        DatePickerDialog(context, { _, y, m, d ->
            eventDate = "${d.pad()}/${(m + 1).pad()}/$y"
        }, year, month, day)
    }

    val openDatePickerDialog = remember {
        DatePickerDialog(context, { _, y, m, d ->
            openDate = "${d.pad()}/${(m + 1).pad()}/$y"
        }, year, month, day)
    }

    val closeDatePickerDialog = remember {
        DatePickerDialog(context, { _, y, m, d ->
            closeDate = "${d.pad()}/${(m + 1).pad()}/$y"
        }, year, month, day)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextField(
            value = eventTitle,
            onValueChange = { eventTitle = it },
            label = { Text("Event Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = eventDescription,
            onValueChange = { eventDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Poster Image")
        }

        posterUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { eventDatePickerDialog.show() }) {
            Text("Pick Event Date: $eventDate")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { openDatePickerDialog.show() }) {
            Text("Pick Registration Open Date: $openDate")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { closeDatePickerDialog.show() }) {
            Text("Pick Registration Close Date: $closeDate")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = selectedTag,
            onValueChange = { selectedTag = it },
            label = { Text("Tag") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = registrationLink,
            onValueChange = { registrationLink = it },
            label = { Text("Registration Link") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = registrationFee,
            onValueChange = { registrationFee = it },
            label = { Text("Registration Fee") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                viewModel.createEvent(
                    context = context,
                    title = eventTitle,
                    description = eventDescription,
                    eventDate = eventDate,
                    registrationOpen = openDate,
                    registrationClose = closeDate,
                    location = location,
                    tag = selectedTag,
                    registrationLink = registrationLink,
                    registrationFee = registrationFee,
                    posterUri = posterUri
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && eventTitle.isNotBlank() && eventDate.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Create Event")
            }
        }

        if (qrCodeBitmap != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                bitmap = qrCodeBitmap!!.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

fun Int.pad() = this.toString().padStart(2, '0')


