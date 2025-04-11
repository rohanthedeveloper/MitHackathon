package com.example.mithackathon.presentation.Auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class AuthViewModel : ViewModel() {
    private val auth  = FirebaseAuth.getInstance()
    private val firestordb = Firebase.firestore

    fun SignIn(email: String, password: String, onresult: (Boolean, String?) -> Unit) {
        if (!email.endsWith("@viit.ac.in")) {
            onresult(false, "UnAuthorised Email ID : Use official VIIT Email Id")
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = task.result?.user?.uid
                uid?.let { saveFcmToken(it) }
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
            onresult(false, "UnAuthorised Email ID : Use official VIIT Email Id")
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = task.result?.user?.uid
                saveUserToDataStore(uid, email, role, userName, PRNno, onresult)
                uid?.let { saveFcmToken(it) }
                onresult(true, null)
            } else {
                onresult(false, task.exception?.message)
            }
        }
    }

    private fun saveUserToDataStore(
        uid: String?,
        email: String,
        role: String,
        userName: String,
        PRNno: String,
        onresult: (Boolean, String?) -> Unit
    ) {
        if (uid == null) {
            onresult(false, "User ID not found")
            return
        }

        val userMap = hashMapOf(
            "email" to email,
            "prn" to PRNno,
            "role" to role,
            "username" to userName
        )

        firestordb.collection("users")
            .document(uid)
            .set(userMap)
            .addOnSuccessListener {
                onresult(true, null)
            }
            .addOnFailureListener {
                onresult(false, "Data of user not stored!!")
            }
    }

    private fun saveFcmToken(uid: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            firestordb.collection("users").document(uid)
                .update("fcmToken", token)
        }.addOnFailureListener {
            Log.e("FCM", "Token fetch failed", it)
        }
    }
}
