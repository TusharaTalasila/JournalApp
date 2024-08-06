package com.example.journalapp.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.example.journalapp.feature.entries.LandingScreenEvent
import com.example.journalapp.feature.entries.LandingScreenUiState
import com.example.journalapp.feature.entry.EntryCreationUiState

import com.example.journalapp.feature.entry.EntryCreationViewModel
import com.example.journalapp.feature.entrydisplay.EntryDisplayScreen
import com.example.journalapp.model.JournalEntry
import com.example.journalapp.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EntryCard(
    modifier: Modifier,
    entry: JournalEntry,
    uiState: LandingScreenUiState,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    val entryCreationViewModel = EntryCreationViewModel()//to get prompts
    val imagePainter = rememberImagePainter(data = entry.imageUrl)
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    val dateString = entry.dateCreated?.toDate()?.let {
        formatter.format(it)
    } ?: ""
    Card(modifier = modifier
        .graphicsLayer {
            // Adjust the camera distance to enhance the 3D effect
            cameraDistance = 8 * density
        }
        .pointerInput(entry) { // Use pointerInput for detecting gestures
            detectTapGestures(
                onDoubleTap = {
                    // Handle double-click here
                    onDoubleClick()
                },
                onTap = {
                    // Handle single-click here
                    onClick()
                }
            )
        }) {
        if (uiState.isFlipped) {
            //display when isFlipped == true
            EntryDisplayScreen(viewModel = entryCreationViewModel, entry = entry)

        } else {
            //display when isFlipped == false
            Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.secondary)) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(if (uiState.selectedEntry != null) .85f else .7f) // Adjust this weight as needed to make the image smaller
                        .fillMaxWidth()
                )
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small)
                        .weight(if (uiState.selectedEntry != null) .15f else .3f) // Allocate the remaining space to the text
                        .fillMaxWidth()
                ) {
                    Text(
                        text = entry.entryName,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun EntryList(
    entries: List<JournalEntry>,
    uiState: LandingScreenUiState, // Replace with actual type
    onHandleEvent: (LandingScreenEvent) -> Unit,
) {
    val scrollState: LazyListState = rememberLazyListState()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val entryToDelete = remember { mutableStateOf<JournalEntry?>(null) }

    Box(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
        LazyColumn(state = scrollState) {
            if (entries.isNotEmpty()) {
                items(entries.chunked(2)) { chunk ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.small),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (entry in chunk) {
                            EntryCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(2.45f / 3f),
                                entry = entry,
                                uiState = uiState,
                                onClick = { onHandleEvent(LandingScreenEvent.EntryCardClicked(entry)) },
                                onDoubleClick = {
                                    entryToDelete.value = entry
                                    showDeleteDialog.value = true
                                }
                            )
                            if (chunk.size == 2 && entry != chunk.last()) {
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            }
                        }
                        if (chunk.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                }
            }
        }
        // This is the correct place to have your AlertDialog - outside of the LazyColumn and loops
        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog.value = false
                    entryToDelete.value = null
                },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete this entry?") },
                confirmButton = {
                    Button(
                        onClick = {
                            entryToDelete.value?.let { entry ->
                                onHandleEvent(LandingScreenEvent.EntryDeleted(entry))
                            }
                            showDeleteDialog.value = false
                            entryToDelete.value = null
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDeleteDialog.value = false
                        entryToDelete.value = null
                    }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

// Assuming `MaterialTheme.spacing` and `LandingScreenEvent` are defined in your codebase.



@Composable
fun EnlargedEntryView(
    uiState: LandingScreenUiState,
    entry: JournalEntry,
    onCardReset: () -> Unit,
    onCardFlip: () -> Unit
) {
    // This Box acts as the overlay background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))//makes the background greyed out
            .clickable(onClick = onCardReset), // Dismiss the overlay when clicked outside the entry view
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onCardReset,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.large
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = (MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }
        // Display Enlarged Card
        EntryCard(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f)
                .fillMaxHeight(fraction = 0.7f),
            entry = entry,
            uiState = uiState,
            onClick = onCardFlip,
            onDoubleClick = {}
        )
    }
}
