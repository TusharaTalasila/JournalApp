package com.example.journalapp.model

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import java.util.Date

data class JournalEntry(
    var journalId: String = "",
    var userId: String = "",
    var dateCreated: Timestamp? = null,
    var imageUrl: String = "",
    var frqAnswers: Map<String, String> = mapOf(),
    var mcAnswers: List<Map<String,Boolean>> = mutableListOf()
)