package com.example.journalapp.network

import com.example.journalapp.model.JournalEntry
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface DBService {
    suspend fun getJournalEntries(currentUserUid: String) : List<JournalEntry>
}