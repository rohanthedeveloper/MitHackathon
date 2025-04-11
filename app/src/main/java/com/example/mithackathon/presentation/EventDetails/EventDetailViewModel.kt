package com.example.mithackathon.presentation.EventDetails


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mithackathon.data.models.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.jvm.java

class EventDetailViewModel (eventId : String): ViewModel() {
    var eventState by mutableStateOf<Event?>(null)
        private set

    init {
        Firebase.firestore.collection("events")
            .document(eventId)
            .get()
            .addOnSuccessListener { document ->
                eventState = document.toObject(Event::class.java)
            }
            .addOnFailureListener {
                Log.e("EventDetail", "Error fetching event", it)
            }
    }
    fun rsvpToEventIfNotAlready(
        eventId: String,
        userId: String,
        name: String,
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userRef = Firebase.firestore
            .collection("event_registrations")
            .document(eventId)
            .collection("users")
            .document(userId)

        userRef.get().addOnSuccessListener { doc ->
            if (!doc.exists()) {
                userRef.set(
                    mapOf(
                        "name" to name,
                        "email" to email
                    )
                ).addOnSuccessListener {
                    onResult(true, null)
                }.addOnFailureListener {
                    onResult(false, it.message)
                }
            } else {
                onResult(false, "Already registered")
            }
        }.addOnFailureListener {
            onResult(false, it.message)
        }
    }

//    fun rsvpToEventIfNotAlready(
//        eventId: String,
//        userId: String,
//        name: String,
//        email: String,
//        onResult: (Boolean, String?) -> Unit
//    ) {
//        val db = Firebase.firestore
//        val userDocRef = db.collection("event_registrations")
//            .document(eventId)
//            .collection("users")
//            .document(userId)
//
//        userDocRef.get()
//            .addOnSuccessListener { document ->
//                if (document.exists()) {
//                    // Already registered
//                    onResult(false, "You have already RSVPed.")
//                } else {
//                    // Not registered, add the RSVP
//                    val data = mapOf(
//                        "name" to name,
//                        "email" to email,
//                        "timestamp" to System.currentTimeMillis()
//                    )
//                    userDocRef.set(data)
//                        .addOnSuccessListener {
//                            onResult(true, null)
//                        }
//                        .addOnFailureListener {
//                            onResult(false, it.message)
//                        }
//                }
//            }
//            .addOnFailureListener {
//                onResult(false, it.message)
//            }
//    }

}