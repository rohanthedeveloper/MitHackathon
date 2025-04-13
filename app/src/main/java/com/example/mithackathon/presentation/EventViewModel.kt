package com.example.mithackathon.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mithackathon.data.Repositories.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import com.example.mithackathon.data.models.Event
import com.example.mithackathon.data.models.User
import com.example.mithackathon.utils.QRCodeUtils
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import android.net.Uri
import com.example.mithackathon.claudinary.CloudinaryHelper
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

//class AddEventViewModel : ViewModel() {
//    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
//   val qrCodeBitmap: StateFlow<Bitmap?> = _qrCodeBitmap
//
//    private val _registrations = MutableStateFlow<List<User>>(emptyList())
//        val registrations: StateFlow<List<User>> = _registrations
//
//    private val _events = MutableStateFlow<List<Event>>(emptyList())
//    val events: StateFlow<List<Event>> = _events
//
//    private val _currentEvent = MutableStateFlow<Event?>(null)
//    val currentEvent: StateFlow<Event?> = _currentEvent
//
//
//    private val _attendees = MutableStateFlow<List<User>>(emptyList())
//    val attendees: StateFlow<List<User>> = _attendees
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error
//    fun uploadEvent(event: Event, onsuccess:(Boolean, String?)-> Unit){
//
//
//        viewModelScope.launch(Dispatchers.IO){
//            val docref =Firebase.firestore.collection("events")
//                .document()
//            val eventWithID = event.copy(id=docref.id)
//            Log.d("id_check",docref.id)
//
//            //mapping
//            val eventMap = hashMapOf(
//                "id" to eventWithID.id,
//                "name" to event.name,
//                "description" to event.description,
//                "type" to event.type,
//                "location" to event.location,
//                "startDate" to event.startDate,
//                "endDate" to event.endDate,
//                "registrationLink" to event.registrationLink,
//                "registrationFee" to event.registrationFee,
//                "clubName" to event.clubName,
//                "clubUid" to event.clubUid,
//                "imageUrl" to event.imageUrl,
//                "timestamp" to System.currentTimeMillis() // Optional for sorting
//            )
//
//            docref.set(eventMap)
//                .addOnSuccessListener { task ->
//                    onsuccess(true,null)
//                }
//                .addOnFailureListener { task ->
//                    onsuccess(false,task.message)
//                }
//        }
//    }
//    fun uploadToCloudinary(file: File, context: Context, onUploaded: (String?) -> Unit) {
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
//            .addFormDataPart("upload_preset", "clubevents_image")
//            .build()
//
//        val request = Request.Builder()
//            .url("https://api.cloudinary.com/v1_1/dzzglagqm/image/upload")
//            .post(requestBody)
//            .build()
//
//        OkHttpClient().newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                onUploaded(null)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val responseBody = response.body?.string()
//
//                if (response.isSuccessful && responseBody != null) {
//                    try {
//                        val json = JSONObject(responseBody)
//
//                        if (json.has("secure_url")) {
//                            val url = json.getString("secure_url")
//                            onUploaded(url)
//                        } else {
//                            Log.e("Cloudinary", "secure_url not found. Full response: $json")
//                            onUploaded(null)
//                        }
//                    } catch (e: JSONException) {
//                        Log.e("Cloudinary", "JSON parsing error: ${e.message}. Response: $responseBody")
//                        onUploaded(null)
//                    }
//                } else {
//                    Log.e("Cloudinary", "Upload failed. Code: ${response.code}, Body: $responseBody")
//                    onUploaded(null)
//                }
//            }
//
//        })
//    }
//    fun uriToFile(context: Context, uri: Uri): File? {
//        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
//        val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
//        file.outputStream().use { output ->
//            inputStream.copyTo(output)
//        }
//        return file
//    }
//
//
//}




class EventViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _registrations = MutableStateFlow<List<User>>(emptyList())
    val registrations: StateFlow<List<User>> = _registrations

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _currentEvent = MutableStateFlow<Event?>(null)
    val currentEvent: StateFlow<Event?> = _currentEvent

    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
    val qrCodeBitmap: StateFlow<Bitmap?> = _qrCodeBitmap

    private val _attendees = MutableStateFlow<List<User>>(emptyList())
    val attendees: StateFlow<List<User>> = _attendees

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun createEvent(
        context: Context,
        title: String,
        description: String,
        eventDate: String,
        registrationOpen: String,
        registrationClose: String,
        location: String,
        tag: String,
        registrationLink: String,
        registrationFee: String,
        posterUri: Uri?,
        clubname : String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                _error.value = "User not authenticated"
                _isLoading.value = false
                return@launch
            }

            try {
                var posterUrl = ""

                if (posterUri != null) {
                    val file = CloudinaryHelper.uriToFile(context, posterUri)
                    if (file != null) {
                        val deferredUrl = CompletableDeferred<String?>()
                        CloudinaryHelper.uploadToCloudinary(file) { url ->
                            deferredUrl.complete(url)
                        }
                        posterUrl = deferredUrl.await() ?: ""
                    }
                }

                val newEvent = Event(
                    name = title,
                    description = description,
                    date = eventDate,
                    registrationOpen = registrationOpen,
                    registrationClose = registrationClose,
                    location = location,
                    tag = tag,
                    registrationLink = registrationLink,
                    registrationFee = registrationFee,
                    imageUrl = posterUrl,
                    organizerId = userId,
                    clubName = clubname,
                    startDate =eventDate
                )

                val eventIdResult = repository.createEvent(newEvent)

                eventIdResult.onSuccess { eventId ->
                    val deepLink = QRCodeUtils.generateDeepLink(eventId)
                    val qrCode = QRCodeUtils.generateQRCode(deepLink, 500)
                    _qrCodeBitmap.value = qrCode
                    loadEvent(eventId)
                }.onFailure {
                    _error.value = it.message
                }

            } catch (e: Exception) {
                _error.value = e.message
            }

            _isLoading.value = false
        }
    }


    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getEvent(eventId)
                .onSuccess { event ->
                    _currentEvent.value = event
                    loadEventAttendees(eventId)
                }
                .onFailure {
                    _error.value = it.message
                }

            _isLoading.value = false
        }
    }

    fun loadUserEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                _error.value = "User not authenticated"
                _isLoading.value = false
                return@launch
            }

            repository.getEventsOrganizedByUser(userId)
                .onSuccess { _events.value = it }
                .onFailure { _error.value = it.message }

            _isLoading.value = false
        }
    }

    private fun loadEventAttendees(eventId: String) {
        viewModelScope.launch {
            repository.getEventAttendees(eventId)
                .onSuccess { _attendees.value = it }
                .onFailure { _error.value = it.message }
        }
    }

    fun generateQRCode(eventId: String) {
        val deepLink = QRCodeUtils.generateDeepLink(eventId)
        val qrCode = QRCodeUtils.generateQRCode(deepLink, 500)
        _qrCodeBitmap.value = qrCode
    }
    fun loadEventRegistrations(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = try {
                val usersSnapshot = Firebase.firestore
                    .collection("event_registrations")
                    .document(eventId)
                    .collection("users")
                    .get()
                    .await()

                val registrations = usersSnapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val email = doc.getString("email")
                    val id = doc.id
                    if (name != null && email != null) {
                        User(id = id, name = name, email = email)
                    } else null
                }

                Result.success(registrations)
            } catch (e: Exception) {
                Result.failure(e)
            }

            result.onSuccess {
                _registrations.value = it
            }.onFailure {
                _error.value = it.message
            }

            _isLoading.value = false
        }
    }





    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            repository.deleteEvent(eventId)
                .onFailure { _error.value = it.message }
        }
    }
}
