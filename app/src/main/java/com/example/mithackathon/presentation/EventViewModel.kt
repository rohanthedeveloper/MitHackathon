package com.example.mithackathon.presentation

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

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
        title: String,
        description: String,
        eventDate: String,
        registrationOpen: String,
        registrationClose: String,
        location: String,
        tag: String,
        registrationLink: String,
        registrationFee: String,
        posterUri: Uri?
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
                val posterUrl = posterUri?.let { uri ->
                    repository.uploadImageToFirebaseStorage(uri)
                } ?: ""

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
                    organizerId = userId
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
