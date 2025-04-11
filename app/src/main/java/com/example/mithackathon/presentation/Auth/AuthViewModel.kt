package com.example.mithackathon.presentation.Auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestordb = Firebase.firestore

    fun SignIn(email: String, password: String, onresult: (Boolean, String?) -> Unit) {
        if (!email.endsWith("@viit.ac.in")) {
            onresult(false, "UnAuthorised Email ID: Use official VIIT Email Id")
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onresult(true, null)
            } else {
                onresult(false, task.exception?.message)
            }
        }
    }

    fun SignUp(
        email: String,
        password: String,
        role: String,
        userName: String,
        PRNno: String,
        onresult: (Boolean, String?) -> Unit
    ) {
        if (!email.endsWith("@viit.ac.in")) {
            onresult(false, "UnAuthorised Email ID: Use official VIIT Email Id")
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = task.result?.user?.uid
                if (uid != null) {
                    saveUserToDataStore(uid, email, role, userName, PRNno, onresult)
                } else {
                    onresult(false, "User ID not found")
                }
            } else {
                onresult(false, task.exception?.message)
            }
        }
    }

    private fun saveUserToDataStore(
        uid: String,
        email: String,
        role: String,
        userName: String,
        PRNno: String,
        onresult: (Boolean, String?) -> Unit
    ) {
        val userMap = hashMapOf(
            "email" to email,
            "prn" to PRNno,
            "role" to role,
            "username" to userName,
        )

        firestordb.collection("users")
            .document(uid)
            .set(userMap)
            .addOnSuccessListener {
                saveAccordingToRole(uid, email, role, userName)
                onresult(true, null)
            }
            .addOnFailureListener {
                onresult(false, "Data of user not stored!!")
            }
    }

    private fun saveAccordingToRole(
        uid: String,
        email: String,
        role: String,
        userName: String,
    ) {
        val imageUri =
            "https://res.cloudinary.com/dzzglagqm/image/upload/v1744380093/307ce493-b254-4b2d-8ba4-d12c080d6651_cg9rg8.jpg"
        val userMap = hashMapOf(
            "email" to email,
            "imageUri" to imageUri,
            "username" to userName
        )

        when (role.trim().lowercase()) {
            "student" -> {
                firestordb.collection("students")
                    .document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Saved to students collection")
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Failed to save to students", it)
                    }
            }

            "club" -> {
                firestordb.collection("clubs")
                    .document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Saved to clubs collection")
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Failed to save to clubs", it)
                    }
            }

            else -> {
                Log.e("Firestore", "Invalid role: $role")
            }
        }
    }
}
