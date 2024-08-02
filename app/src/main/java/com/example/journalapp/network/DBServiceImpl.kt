package com.example.journalapp.network

import com.example.journalapp.model.JournalEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.E

class DBServiceImpl() : DBService {

    override suspend fun getJournalEntries(currentUserUid: String) = suspendCoroutine { cont ->
        if (currentUserUid.isEmpty()) {
            cont.resumeWithException(IllegalArgumentException("User UID can't be null"))
        }

        FirebaseFirestore.getInstance().collection("users").document(currentUserUid)
            .collection("my_entries").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dbEntries = task.getResult()
                    //map each entry to a JournalEntry object
                    val entries = dbEntries.documents.mapNotNull { document ->
                        try {
                            document.toObject<JournalEntry>()?.apply { journalId = document.id }
                        } catch (e: Exception) {
                            null // Skip any documents that can't be deserialized
                        }
                    }
                    cont.resume(entries)
                } else {
                    cont.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                }
            }
    }

}