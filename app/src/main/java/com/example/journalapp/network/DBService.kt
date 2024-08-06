package com.example.journalapp.network

import android.os.Parcelable

import com.example.journalapp.model.JournalEntry


interface DBService {
    suspend fun getJournalEntries(currentUserUid: String) : List<JournalEntry>
    fun deleteJournalEntry(journalEntry: JournalEntry)

}