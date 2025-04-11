package com.example.mithackathon.data.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val eventsOrganized: List<String> = emptyList(),
    val eventsAttended: List<String> = emptyList(),
    val fcmToken: String = ""
)

