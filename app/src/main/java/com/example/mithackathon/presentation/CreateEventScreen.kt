package com.example.mithackathon.presentation

//import android.net.Uri
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.compose.runtime.*
//import androidx.compose.material.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.navigation.NavController
//import com.google.zxing.BarcodeFormat
//import com.journeyapps.barcodescanner.BarcodeEncoder
//import java.util.Calendar
//import android.widget.DatePicker
//import android.app.DatePickerDialog
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Upload
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.SegmentedButtonDefaults.Icon
//import androidx.compose.material3.Surface
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.sp
//import coil.compose.rememberAsyncImagePainter


import android.R.attr.text
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.service.autofill.OnClickAction
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mithackathon.InternalFun.getUserInfoFromPrefs
import com.example.mithackathon.data.models.Event
import com.example.mithackathon.data.models.EventFormState
import com.example.mithackathon.data.models.validateForm
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.String

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddEventScreen(viewModel: AddEventViewModel,
//                   onClickAction: ()-> Unit , navController: NavController){
////    val context = LocalContext.current
////    val datePickerDialog = remember {
////        DatePickerDialog(context, { _, year, month, dayOfMonth ->
////            eventDate = "$dayOfMonth/${month + 1}/$year"
////        }, year, month, day)
////    }
//
//    var eventDate by remember { mutableStateOf("") }
//    var closeDate by remember { mutableStateOf("") }
//    var openDate by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//    val calendar = Calendar.getInstance()
//
//    val year = calendar.get(Calendar.YEAR)
//    val month = calendar.get(Calendar.MONTH)
//    val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//    // Event Date Picker
//    val eventDatePickerDialog = remember {
//        DatePickerDialog(
//            context,
//            { _, selectedYear, selectedMonth, selectedDay ->
//                eventDate = "${selectedDay.toString().padStart(2, '0')}/" +
//                        "${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
//            },
//            year,
//            month,
//            day
//        )
//    }
//    // registration open date Picker
//    val OpenDatePickerDialog = remember {
//        DatePickerDialog(
//            context,
//            { _, selectedYear, selectedMonth, selectedDay ->
//                openDate = "${selectedDay.toString().padStart(2, '0')}/" +
//                        "${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
//            },
//            year,
//            month,
//            day
//        )
//    }
//
//
//// Close Date Picker
//    val closeDatePickerDialog = remember {
//        DatePickerDialog(
//            context,
//            { _, selectedYear, selectedMonth, selectedDay ->
//                closeDate = "${selectedDay.toString().padStart(2, '0')}/" +
//                        "${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
//            },
//            year,
//            month,
//            day
//        )
//    }
//    var eventTitle by remember { mutableStateOf("") }
//    var eventDescription by remember { mutableStateOf("") }
//    var registrationLink by remember { mutableStateOf("") }
//    var selectedTag by remember { mutableStateOf("") }
//    var location by remember{mutableStateOf("")}
//    var RegistrationFee by remember{mutableStateOf("")}
//
//    var cloudinaryImageLink by remember { mutableStateOf("") }
//
//
//    var formState by remember { mutableStateOf(EventFormState()) }
//    val validatedState = validateForm(formState)
//
//
//
//    //logic for image upload
//    // Image URI
//    var posterUri by remember { mutableStateOf<Uri?>(null) }
//
//// Image Picker Launcher
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        posterUri = uri
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F7FA))
//            .verticalScroll(rememberScrollState())
//    ) {
//        // Top bar
//        TopAppBar(
//            title = { Text("Add Event", fontWeight = FontWeight.Medium) },
//            navigationIcon = {
//                IconButton(onClick = { /* Handle back navigation */ }) {
//                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.White
//            ),
//        )
//
//        // Content
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Event Poster
//            Text(
//                text = "Event Poster",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .border(
//                        BorderStroke(1.dp, Color(0xFFCED4DA)),
//                        RoundedCornerShape(12.dp)
//                    )
//                    .background(Color.White)
//                    .clickable { imagePickerLauncher.launch("image/*") },
//                contentAlignment = Alignment.Center
//            ) {
//                if (posterUri != null) {
//                    Image(
//                        painter = rememberAsyncImagePainter(posterUri),
//                        contentDescription = "Event Poster",
//                        contentScale = ContentScale.None,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                } else {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(
//                            imageVector = Icons.Default.Upload,
//                            contentDescription = "Upload",
//                            tint = Color(0xFF4F6A92)
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = "Click here to upload your file",
//                            color = Color(0xFF4F6A92),
//                            fontSize = 14.sp
//                        )
//                    }
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Event title
//            Text(
//                text = "Event title",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = eventTitle,
//                onValueChange = { eventTitle = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp)),
//                shape = RoundedCornerShape(12.dp),
//
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Event Type/Tag
//            Text(
//                text = "Event Type",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Tags Row
//            Row(
//                modifier = Modifier.horizontalScroll(rememberScrollState(),true),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                EventTagChip(
//                    tag = "Hackathon",
//                    isSelected = selectedTag == "Hackathon",
//                    onTagSelected = { selectedTag = "Hackathon" }
//                )
//
//                EventTagChip(
//                    tag = "Seminar",
//                    isSelected = selectedTag == "Seminar",
//                    onTagSelected = { selectedTag = "Seminar" }
//                )
//
//                EventTagChip(
//                    tag = "Workshop",
//                    isSelected = selectedTag == "Workshop",
//                    onTagSelected = { selectedTag = "Workshop" }
//                )
//
//                EventTagChip(
//                    tag = "Other",
//                    isSelected = selectedTag == "Other",
//                    onTagSelected = { selectedTag = "Other" }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Event Description
//            Text(
//                text = "Event Description",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = eventDescription,
//                onValueChange = { eventDescription = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)
//                    .clip(RoundedCornerShape(12.dp)),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Event Date
//            Text(
//                text = "Registration open date",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = openDate,
//                onValueChange = {}, // Disable manual editing
//                label = {Text("Open Date")},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp))
//                    .clickable { OpenDatePickerDialog.show() }, // Open dialog on click
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                ),
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.CalendarToday,
//                        contentDescription = "Select Date",
//                        modifier = Modifier.clickable { OpenDatePickerDialog.show() }
//                    )
//                },
//                readOnly = true
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            // Event Date
//            Text(
//                text = "Event Date",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = eventDate,
//                onValueChange = {}, // Disable manual editing
//                label = {Text("Event Date")},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp))
//                    .clickable { eventDatePickerDialog.show() }, // Open dialog on click
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                ),
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.CalendarToday,
//                        contentDescription = "Select Date",
//                        modifier = Modifier.clickable { eventDatePickerDialog.show() }
//                    )
//                },
//                readOnly = true
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            // Registration close date
//            Text(
//                text = "Registration close date",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//// Close Date Field
//            OutlinedTextField(
//                value = closeDate,
//                onValueChange = {},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp))
//                    .clickable { closeDatePickerDialog.show() },
//                shape = RoundedCornerShape(12.dp),
//                label = { Text("Close Date") },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                ),
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.CalendarToday,
//                        contentDescription = "Select Close Date",
//                        modifier = Modifier.clickable { closeDatePickerDialog.show() }
//                    )
//                },
//                readOnly = true
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Registration Link
//            Text(
//                text = "Registration Link",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = registrationLink,
//                onValueChange = { registrationLink = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp)),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                ),
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Link,
//                        contentDescription = "Registration Link"
//                    )
//                },
//                placeholder = { Text("https://...") }
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "Location",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedTextField(
//                value = location,
//                onValueChange = { location = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp)),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                ),
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.LocationOn,
//                        contentDescription = "location"
//                    )
//                },
//                placeholder = { Text("location") }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            // Event Date
//            Text(
//                text = "Registration Fee",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = RegistrationFee,
//                onValueChange = {RegistrationFee= it}, // Disable manual editing
//                label = {Text("Registration Fee")},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(12.dp))
//                    .clickable { eventDatePickerDialog.show() }, // Open dialog on click
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    unfocusedBorderColor = Color(0xFFCED4DA)
//                ),
//            )
//
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Add Event Button
//            Button(
//                onClick =  {
//                    formState = EventFormState(
//                        name = eventTitle,
//                        description = eventDescription,
//                        type = selectedTag,
//                        location = location,
//                        startDate = openDate,
//                        endDate = closeDate,
//                        registrationLink = registrationLink,
//                        registrationFee = RegistrationFee,
//                        posterImageUri = posterUri
//                    )
//
//                    val updatedState = validateForm(formState)
//                    if (listOf(
//                            updatedState.nameError,
//                            updatedState.descriptionError,
//                            updatedState.typeError,
//                            updatedState.locationError,
//                            updatedState.startDateError,
//                            updatedState.endDateError
//                        ).all { it == null }
//                    ) {
//                        val (uid, name, type) = getUserInfoFromPrefs(context)
//
//                        val file = viewModel.uriToFile(context, formState.posterImageUri!!)
//
//                        if (file != null) {
//                            viewModel.uploadToCloudinary(file, context) { cloudinaryImageUrl ->
//                                if (cloudinaryImageUrl != null) {
//                                    // Proceed after image is uploaded
//                                    val event = Event(
//                                        name = formState.name,
//                                        description = formState.description,
//                                        type = formState.type,
//                                        location = formState.location,
//                                        startDate = formState.startDate,
//                                        endDate = formState.endDate,
//                                        registrationLink = formState.registrationLink,
//                                        registrationFee = formState.registrationFee,
//                                        clubName = name,
//                                        clubUid = uid,
//                                        imageUrl = cloudinaryImageUrl
//                                    )
//
//                                    viewModel.uploadEvent(event) { success, error ->
//                                        if (success) {
//                                            Toast.makeText(context, "Event uploaded successfully", Toast.LENGTH_SHORT).show()
//                                            eventTitle = ""
//                                            eventDescription = ""
//                                            registrationLink = ""
//                                            selectedTag = ""
//                                            location = ""
//                                            RegistrationFee = ""
//                                            eventDate = ""
//                                            openDate = ""
//                                            closeDate = ""
//                                            posterUri = null
//                                            cloudinaryImageLink = ""
//                                            formState = EventFormState()
//
//                                        } else {
//                                            Toast.makeText(context, error ?: "Upload failed", Toast.LENGTH_SHORT).show()
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        } else {
//                            Toast.makeText(context, "Invalid image file", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        // Show validation errors
//                        formState = updatedState
//                        Toast.makeText(context, "Fill all required fields", Toast.LENGTH_SHORT).show()
//                    }
//
//
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF0A2647)
//                )
//            ) {
//                Text(
//                    text = "Add Event",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//        }
//    }
//}
//
//@Composable
//fun EventTagChip(
//    tag: String,
//    isSelected: Boolean,
//    onTagSelected: () -> Unit
//) {
//    val backgroundColor = if (isSelected) Color(0xFF0A2647) else Color.White
//    val textColor = if (isSelected) Color.White else Color(0xFF0A2647)
//    val borderColor = Color(0xFF0A2647)
//
//    Surface(
//        modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .clickable { onTagSelected() },
//        shape = RoundedCornerShape(16.dp),
//        color = backgroundColor,
//        border = BorderStroke(1.dp, borderColor)
//    ) {
//        Text(
//            text = tag,
//            color = textColor,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
//        )
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun AddEventScreenPreview() {
//    MaterialTheme {
//        AddEventScreen(viewModel())
//    }
//}



@OptIn(ExperimentalMaterial3Api::class)
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
            .statusBarsPadding()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TopAppBar(title = {Text("Add Event")},
            )
        OutlinedTextField(
            value = eventTitle,
            onValueChange = { eventTitle = it },
            label = { Text("Event Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = eventDescription,
            onValueChange = { eventDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Event Poster" , fontSize = 16.sp , fontWeight = FontWeight.Bold)

//        posterUri?.let {
//            Spacer(modifier = Modifier.height(8.dp))
//            Image(
//                painter = rememberAsyncImagePainter(it),
//                contentDescription = "Selected Poster",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//            )
//        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    BorderStroke(1.dp, Color(0xFFCED4DA)),
                    RoundedCornerShape(12.dp)
                )
                .background(Color.White)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (posterUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(posterUri),
                    contentDescription = "Event Poster",
                    contentScale = ContentScale.None,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Upload,
                        contentDescription = "Upload",
                        tint = Color(0xFF4F6A92)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Click here to upload your file",
                        color = Color(0xFF4F6A92),
                        fontSize = 14.sp
                    )
                }
            }
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

        Text(
            text = "Event Type",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

// Tags Row
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState(),true),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EventTagChip(
                tag = "Hackathon",
                isSelected = selectedTag == "Hackathon",
                onTagSelected = { selectedTag = "Hackathon" }
            )

            EventTagChip(
                tag = "Seminar",
                isSelected = selectedTag == "Seminar",
                onTagSelected = { selectedTag = "Seminar" }
            )

            EventTagChip(
                tag = "Workshop",
                isSelected = selectedTag == "Workshop",
                onTagSelected = { selectedTag = "Workshop" }
            )

            EventTagChip(
                tag = "Other",
                isSelected = selectedTag == "Other",
                onTagSelected = { selectedTag = "Other" }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = registrationLink,
            onValueChange = { registrationLink = it },
            label = { Text("Registration Link") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
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
              val clubname=  getUserInfoFromPrefs(context).name
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
                    posterUri = posterUri,
                    clubname
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


@Composable
fun EventTagChip(
    tag: String,
    isSelected: Boolean,
    onTagSelected: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF0A2647) else Color.White
    val textColor = if (isSelected) Color.White else Color(0xFF0A2647)
    val borderColor = Color(0xFF0A2647)

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTagSelected() },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = tag,
            color = textColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
