package com.example.mithackathon.data.Repositories

import android.net.Uri
import com.example.mithackathon.data.models.Event
import com.example.mithackathon.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val eventsCollection = db.collection("events")
    private val usersCollection = db.collection("users")
    private val auth = FirebaseAuth.getInstance()

    suspend fun createEvent(event: Event): Result<String> = withContext(Dispatchers.IO) {
        try {
            val eventId = eventsCollection.document().id
            val newEvent = event.copy(id = eventId)
            eventsCollection.document(eventId).set(newEvent).await()

            val userId = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("User not authenticated"))
            val userDoc = usersCollection.document(userId).get().await()

            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)
                val updatedEvents = user?.eventsOrganized?.toMutableList() ?: mutableListOf()
                updatedEvents.add(eventId)
                usersCollection.document(userId).update("eventsOrganized", updatedEvents).await()
            }

            Result.success(eventId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEvent(eventId: String): Result<Event> = withContext(Dispatchers.IO) {
        try {
            val doc = eventsCollection.document(eventId).get().await()
            if (!doc.exists()) return@withContext Result.failure(Exception("Event not found"))
            val event = doc.toObject(Event::class.java)
                ?: return@withContext Result.failure(Exception("Failed to parse event"))
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAttendance(eventId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val eventDoc = eventsCollection.document(eventId).get().await()
            if (!eventDoc.exists()) return@withContext Result.failure(Exception("Event not found"))

            val event = eventDoc.toObject(Event::class.java)
            val updatedAttendees = event?.attendees?.toMutableSet() ?: mutableSetOf()

            if (!updatedAttendees.add(userId)) {
                return@withContext Result.failure(Exception("Attendance already marked"))
            }

            eventsCollection.document(eventId).update("attendees", updatedAttendees.toList()).await()

            val userDoc = usersCollection.document(userId).get().await()
            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)
                val updatedEvents = user?.eventsAttended?.toMutableSet() ?: mutableSetOf()
                updatedEvents.add(eventId)
                usersCollection.document(userId).update("eventsAttended", updatedEvents.toList()).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventsOrganizedByUser(userId: String): Result<List<Event>> = withContext(Dispatchers.IO) {
        try {
            val userDoc = usersCollection.document(userId).get().await()
            if (!userDoc.exists()) return@withContext Result.failure(Exception("User not found"))

            val user = userDoc.toObject(User::class.java)
            val eventIds = user?.eventsOrganized ?: emptyList()

            val events = eventIds.mapNotNull { eventId ->
                val doc = eventsCollection.document(eventId).get().await()
                if (doc.exists()) doc.toObject(Event::class.java) else null
            }

            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventAttendees(eventId: String): Result<List<User>> = withContext(Dispatchers.IO) {
        try {
            val doc = eventsCollection.document(eventId).get().await()
            if (!doc.exists()) return@withContext Result.failure(Exception("Event not found"))

            val event = doc.toObject(Event::class.java)
            val attendeeIds = event?.attendees ?: emptyList()

            val users = attendeeIds.mapNotNull { userId ->
                val userDoc = usersCollection.document(userId).get().await()
                if (userDoc.exists()) userDoc.toObject(User::class.java) else null
            }

            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun uploadImageToFirebaseStorage(uri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val storageRef = FirebaseStorage.getInstance().reference
            val fileName = UUID.randomUUID().toString()
            val imageRef = storageRef.child("event_posters/$fileName")

            val uploadTask = imageRef.putFile(uri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            return@withContext downloadUrl.toString()
        } catch (e: Exception) {
            throw Exception("Image upload failed: ${e.message}")
        }
    }


}
