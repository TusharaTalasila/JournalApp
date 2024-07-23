package com.example.journalapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.journalapp.feature.auth.AuthViewModel
import com.example.journalapp.feature.entry.EntryCreationScreen
import com.example.journalapp.feature.entry.EntryCreationViewModel
import com.example.journalapp.feature.navgraph.AppNavigation
import com.example.journalapp.ui.theme.JournalTheme
import com.google.firebase.BuildConfig

class MainActivity : ComponentActivity() {
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val entryCreationViewModel = EntryCreationViewModel()
        val authViewModel : AuthViewModel by viewModels()
        setContent {
            JournalTheme {//apply theme to composable
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding->
                   AppNavigation(modifier = Modifier.padding(innerPadding),authViewModel = authViewModel, entryCreationViewModel = entryCreationViewModel)
                }

            }
        }
    }
}


