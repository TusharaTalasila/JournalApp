package com.example.journalapp.feature.entry

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.journalapp.core.components.EntryDisplay
import com.example.journalapp.core.components.PagingEntries
import com.example.journalapp.ui.theme.spacing


@Composable
fun EntryCreationScreen(
    onBack: () -> Unit,
    viewModel: EntryCreationViewModel,
    onHandleEvent: (EntryCreationScreenEvent) -> Unit,
    navController: NavController,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    // control the visibility of the warning dialog
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MaterialTheme.spacing.large,
                    vertical = MaterialTheme.spacing.xxLarge
                )
        ) {

            Button(onClick = { setShowDialog(true) }) {
                Text(
                    text = "Back",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Conditionally show the dialog based on the showDialog state
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        setShowDialog(false)
                    },
                    title = {
                        Text(text = "Warning")
                    },
                    text = {
                        Text(text = "Are you sure you want to go back? All answers and image will be lost")//todo: modify later
                    },
                    //actions for if user does go back
                    confirmButton = {
                        Button(
                            onClick = {
                                // Handle the back action
                                onBack()
                                setShowDialog(false) // Dismiss the dialog
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    //action if user doesn't wanna go back
                    dismissButton = {
                        Button(
                            onClick = {
                                setShowDialog(false)
                            }
                        ) {
                            Text("No")
                        }
                    }
                )
            }


            Row(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.large,
                    // bottom = MaterialTheme.spacing.xxLarge5,
                    start = MaterialTheme.spacing.small,
                    end = MaterialTheme.spacing.small
                )
            ) {
                PagingEntries(navController, onHandleEvent, uiState.value)
            }
        }

    }

}


