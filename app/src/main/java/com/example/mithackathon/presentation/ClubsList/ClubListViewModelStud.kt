package com.example.mithackathon.presentation.ClubsList

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mithackathon.data.models.Club
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ClubViewModel : ViewModel() {

    private val firestordb = Firebase.firestore

    var clubsList by mutableStateOf<List<ClubUser>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchClubs()
    }

    fun fetchClubs() {
        isLoading = true
        errorMessage = null

        firestordb.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val clubs = result.documents.mapNotNull { doc ->
                    val role = doc.getString("role")
                    if (role == "Club") {
                        val id = doc.id
                        val name = doc.getString("username") ?: "Unknown"
                        val email = doc.getString("email") ?: "N/A"
                        val imageUri = doc.getString("imageUri") ?: ""
                        ClubUser(id, name, email, imageUri)
                    } else null
                }
                clubsList = clubs
                isLoading = false
            }
            .addOnFailureListener {
                errorMessage = it.message ?: "Error fetching clubs"
                isLoading = false
            }
    }
}

data class ClubUser(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val imageUri: String = ""
)
