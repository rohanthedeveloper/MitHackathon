package com.example.mithackathon.data.models

data class Event(
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val registrationOpen: String = "",
    val registrationClose: String = "",
    val location: String = "",
    val tag: String = "",
    val registrationLink: String = "",
    val registrationFee: String = "",
    val imageUrl: String = "",
    val organizerId: String = "",
    val id: String = "" ,// optional, if needed
    val attendees: List<String> = emptyList(),
    val clubName: String = "",     // fetched from SharedPrefs
    val clubUid: String = "",
    val type: String = "",
    val startDate: String = "",
    val endDate: String = "",
)
