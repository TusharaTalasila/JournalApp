package com.example.journalapp.feature.navgraph

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.journalapp.feature.auth.AuthViewModel
import com.example.journalapp.feature.auth.EmailLoginScreen
import com.example.journalapp.feature.auth.SignUpScreen
import com.example.journalapp.feature.entries.LandingScreen
import com.example.journalapp.feature.entries.LandingViewModel
import com.example.journalapp.feature.entry.EntryCreationScreen
import com.example.journalapp.feature.entry.EntryCreationViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    entryCreationViewModel: EntryCreationViewModel,
    landingViewModel: LandingViewModel
) {//Todo: Add in VM's for all 4 screens
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        builder = {//routing is done within the screens themselves (by calling a route)
            composable("login") {
                EmailLoginScreen(modifier, navController, authViewModel)
            }
            composable("signup") {
                SignUpScreen(modifier, navController, authViewModel)
            }
            composable("landing"){
                LaunchedEffect(Unit) {
                    landingViewModel.init()
                }
                LandingScreen(modifier,navController,authViewModel, landingViewModel, onHandleEvent = landingViewModel::handleEvent )
            }
            composable("entryCreation"){
                EntryCreationScreen(onBack = navController::navigateUp, viewModel = entryCreationViewModel, onHandleEvent = entryCreationViewModel::handleEvent, navController = navController)
            }
        })

}



