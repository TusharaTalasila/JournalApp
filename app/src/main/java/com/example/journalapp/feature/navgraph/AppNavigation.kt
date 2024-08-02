package com.example.journalapp.feature.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                LandingScreen(modifier,navController,authViewModel, landingViewModel)
            }
            composable("entryCreation"){
                EntryCreationScreen(onBack = navController::navigateUp, viewModel = entryCreationViewModel, onHandleEvent = entryCreationViewModel::handleEvent, navController = navController)
            }

        })

}