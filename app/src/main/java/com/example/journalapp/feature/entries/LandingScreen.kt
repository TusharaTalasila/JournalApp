package com.example.journalapp.feature.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.journalapp.feature.auth.AuthState
import com.example.journalapp.feature.auth.AuthViewModel
import com.example.journalapp.feature.entry.EntryCreationViewModel

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    entryCreationViewModel: EntryCreationViewModel
) {
    val authState = authViewModel.uiState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Page", fontSize = 32.sp)

        TextButton(onClick = { authViewModel.signOut() }) {
            Text(text = "Sign out")
        }
        TextButton(onClick = { navController.navigate("entryCreation") }) {
            Text(text = "Make Entry")
        }
    }
}