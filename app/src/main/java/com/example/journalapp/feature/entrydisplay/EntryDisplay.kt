package com.example.journalapp.feature.entrydisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.journalapp.core.components.EntryPreview
import com.example.journalapp.core.components.PagingEntries
import com.example.journalapp.feature.entry.EntryCreationScreenEvent
import com.example.journalapp.feature.entry.EntryCreationUiState
import com.example.journalapp.feature.entry.EntryCreationViewModel
import com.example.journalapp.model.JournalEntry
import com.example.journalapp.ui.theme.spacing

@Composable
fun EntryDisplayScreen(
    viewModel: EntryCreationViewModel,
    entry: JournalEntry
) {
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
                    vertical = MaterialTheme.spacing.xLarge
                )
        ) {
            Row(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.large,
                    start = MaterialTheme.spacing.small,
                    end = MaterialTheme.spacing.small
                )
            ) {
                EntryPreview(viewModel = viewModel, entry = entry)
            }
        }

    }

}