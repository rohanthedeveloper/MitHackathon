package com.example.mithackathon.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mithackathon.data.Repositories.FirebaseRepository
import com.example.mithackathon.data.models.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    fun markAttendance(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _success.value = false

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                _error.value = "User not authenticated"
                _isLoading.value = false
                return@launch
            }

            repository.markAttendance(eventId, userId)
                .onSuccess {
                    _success.value = true
                    loadEvent(eventId)
                }
                .onFailure {
                    _error.value = it.message
                }

            _isLoading.value = false
        }
    }

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            repository.getEvent(eventId)
                .onSuccess { event ->
                    _event.value = event
                }
                .onFailure {
                    _error.value = it.message
                }
        }
    }

    fun processDeepLink(deepLink: String) {
        val regex = "attendanceapp://event/(.+)".toRegex()
        val matchResult = regex.find(deepLink)

        if (matchResult != null) {
            val eventId = matchResult.groupValues[1]
            markAttendance(eventId)
        } else {
            _error.value = "Invalid QR code"
        }
    }

    fun resetState() {
        _success.value = false
        _error.value = null
    }
}

