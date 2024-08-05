package com.example.journalapp.core.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.journalapp.R
import com.example.journalapp.feature.entry.EntryCreationViewModel
import com.example.journalapp.model.JournalEntry
import com.example.journalapp.ui.theme.spacing

//Component That displays a users responses that were used to generate the entry
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EntryPreview(
    viewModel: EntryCreationViewModel,
    entry: JournalEntry
) {
    val uiState = viewModel.uiState
    val entries = listOf(
        Pair(stringResource(id = R.string.prompt_1), null),
        Pair(stringResource(id = R.string.prompt_2), null),
        Pair(stringResource(id = R.string.prompt_3), null),
        Pair(stringResource(id = R.string.prompt_4), null),
        Pair(stringResource(id = R.string.prompt_5), null),
        Pair(stringResource(id = R.string.prompt_6), null),
        Pair(stringResource(id = R.string.prompt_7), uiState.value.emotions),
        Pair(stringResource(id = R.string.prompt_8), uiState.value.weather),
        Pair(stringResource(id = R.string.prompt_9), uiState.value.artists)
    )
    var pageIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = MaterialTheme.spacing.medium)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Box(contentAlignment = Alignment.Center) {
                when (pageIndex) {
                    in entries.indices -> {
                        //display users answers
                        Entry(
                            index = pageIndex,
                            prompt = entries[pageIndex].first,
                            entry = entry
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        //buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (pageIndex > 0) pageIndex-- },
                enabled = pageIndex > 0,

                ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { if (pageIndex < entries.size - 1) pageIndex++ },
                enabled = pageIndex < entries.size - 1
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        }

    }
}

@Composable
fun Entry(
    index: Int,
    prompt: String,
    entry: JournalEntry,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = MaterialTheme.spacing.xLarge,
                vertical = MaterialTheme.spacing.large
            )
    ) {
        //question
        QuestionDisplay(index = index + 1, prompt = prompt)
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.xLarge))
        if (index < 6) {
            val answer = entry.frqAnswers.getValue(numberToWord(index + 1))
            //FRQ Answer
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background( // Apply background color and shape to Text
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                Text(
                    text = answer,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.large)
                )
            }
        }
        //MCQ
        else {
            MultipleChoiceDisplay(entry.mcAnswers[index - 6])
        }

    }
}

@Composable
fun QuestionDisplay(index: Int, prompt: String) {
    Box(//hold the entire question object
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.45f)
    ) {
        // This is the larger box to display the question
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = MaterialTheme.spacing.small)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = prompt,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
                    .align(Alignment.Center) // Adds padding inside the box for the text
            )
        }
        // This is the smaller box for the question number
        Box(
            modifier = Modifier
                .offset(y = (-25).dp)
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp) // Sets the size of the small box
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ) // Sets the background color of the small box
            ) {
                Text(
                    text = stringResource(id = R.string.questionCt, index),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun MultipleChoiceDisplay(
    mcAnswers: Map<String, Boolean>
) {
    val originalColor = MaterialTheme.colorScheme.tertiary
    val selectedColor = MaterialTheme.colorScheme.secondary
    val scrollState = rememberLazyListState()
    LaunchedEffect(mcAnswers) {
        // uiState recomposes due to the list update
        scrollState.scrollToItem(0)
    }
    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.small)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(MaterialTheme.spacing.small)
            )
    ) {
        //iterate through map of question
        items(mcAnswers.toList()) { entry ->
            val isSelected = entry.second
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(vertical = MaterialTheme.spacing.large)
                    .background(
                        color = if (isSelected) selectedColor else originalColor,
                        shape = RoundedCornerShape(MaterialTheme.spacing.small)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    text = entry.first,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

    }

}

fun numberToWord(number: Int): String {
    return when (number) {
        1 -> "one"
        2 -> "two"
        3 -> "three"
        4 -> "four"
        5 -> "five"
        6 -> "six"
        else -> "unknown"
    }
}
