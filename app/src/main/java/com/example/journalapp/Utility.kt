package com.example.journalapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


    fun getCollectionReferenceForJournalEntries(): CollectionReference?{
        val currentUser = FirebaseAuth.getInstance().currentUser
        return if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid).collection("my_entries")
        } else{
            null
        }
    }
