package com.example.journalapp.feature.entries

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.journalapp.R
import com.example.journalapp.core.components.EnlargedEntryView
import com.example.journalapp.core.components.EntryList
import com.example.journalapp.core.components.SearchBar
import com.example.journalapp.feature.auth.AuthState
import com.example.journalapp.feature.auth.AuthViewModel
import com.example.journalapp.ui.theme.spacing
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    landingViewModel: LandingViewModel,
    onHandleEvent: (LandingScreenEvent) -> Unit,
) {
    val authState = authViewModel.uiState.observeAsState()
    val uiState = landingViewModel.uiState.collectAsStateWithLifecycle()
    var (showFilterDialog, setShowFilterDialog) = remember { mutableStateOf(false) }
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        //Welcome back row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(MaterialTheme.spacing.large)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Row {
                Text(text = "Welcome Back!   ", style = MaterialTheme.typography.displayLarge)
                IconButton(onClick = { authViewModel.signOut() }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "User's button",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp) // You can adjust the size (width and height) here
                    )
                }
            }
        }

        //entry display row
        Column(
            modifier = Modifier
                .padding(
                    bottom = MaterialTheme.spacing.xLarge,
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.large
                )
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .weight(5f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //search bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.medium), // Ensures the Row fills the max width of its parent
                verticalAlignment = Alignment.CenterVertically // Aligns children to the center vertically
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f), // Takes up the remaining space after IconButton
                    onQueryChanged = { query ->
                        onHandleEvent(LandingScreenEvent.SearchQueryChanged(query))
                    },
                    onClear = { onHandleEvent(LandingScreenEvent.SearchCleared) }
                )
                IconButton(onClick = { setShowFilterDialog(true) }) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Filter Entries",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                if (showFilterDialog) {

                    var selectedSortOption by remember { mutableStateOf(SortOption.Unspecified) }
                    //filter dialog
                    AlertDialog(
                        onDismissRequest = { showFilterDialog = false },
                        title = { Text(text = "Filters") },
                        text = {
                            Column() {
                                Text(text = "Sort By", style = MaterialTheme.typography.bodyLarge)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            selectedSortOption = SortOption.NewestFirst
                                        })
                                ) {
                                    RadioButton(
                                        selected = selectedSortOption == SortOption.NewestFirst,
                                        onClick = { selectedSortOption = SortOption.NewestFirst }
                                    )
                                    Text(
                                        text = "Newest first",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(
                                            start = MaterialTheme.spacing.xSmall,
                                            top = MaterialTheme.spacing.medium
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            selectedSortOption = SortOption.OldestFirst
                                        })
                                ) {
                                    RadioButton(
                                        selected = selectedSortOption == SortOption.OldestFirst,
                                        onClick = { selectedSortOption = SortOption.OldestFirst }
                                    )
                                    Text(
                                        text = "Oldest first",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(
                                            start = MaterialTheme.spacing.xSmall,
                                            top = MaterialTheme.spacing.medium
                                        )
                                    )
                                }

                            }
                        },
                        //actions for if user does go back
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (selectedSortOption == SortOption.NewestFirst) {
                                        onHandleEvent(LandingScreenEvent.NewestClicked)
                                    }
                                    if (selectedSortOption == SortOption.OldestFirst) {
                                        onHandleEvent(LandingScreenEvent.OldestClicked)
                                    }
                                    setShowFilterDialog(false) // Dismiss the dialog
                                }
                            ) {
                                Text("Apply", color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        },
                        //action if user doesn't wanna go back
                        dismissButton = {
                            Button(
                                onClick = {
                                    setShowFilterDialog(false)
                                }
                            ) {
                                Text("Cancel", color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    )
                }
            }

            if (uiState.value.filteredEntries.isEmpty() && uiState.value.journalEntries.isNotEmpty()) {
                //add no results found screen
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .weight(1f)
                        ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Place your illustration here
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    Image(
                        painter = painterResource(id = R.drawable.noresults),
                        contentDescription = "Illustration"
                    )
                    Text(
                        text = "Uh Oh, no results found!",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            } else if(uiState.value.filteredEntries.isEmpty() && uiState.value.journalEntries.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(MaterialTheme.spacing.small)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else {
                //entry display
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(MaterialTheme.spacing.small)
                        )
                ) {
                    val entries = uiState.value.filteredEntries
                    EntryList(entries = entries, uiState.value,  onHandleEvent)
                }
            }

        }

        //make entries button row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(MaterialTheme.spacing.large)
                .weight(.75f),
        ) {
            Box(//hold the entire question object
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // This is the larger box to display the question
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .offset(y = (-30).dp)
                        .align(Alignment.TopCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp) // Sets the size of the small box
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ) // Sets the background color of the small box
                    ) {
                        IconButton(onClick = { navController.navigate("entryCreation") }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "User's button",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(30.dp) // You can adjust the size (width and height) here
                            )
                        }
                    }
                }
            }
        }

    }
    uiState.value.selectedEntry?.let { entry ->
        EnlargedEntryView(
            entry = entry,
            uiState = uiState.value,
            onCardReset = { onHandleEvent(LandingScreenEvent.EntryCardReset) },
            onCardFlip = { onHandleEvent(LandingScreenEvent.EntryCardFlipped) })
    }

}




