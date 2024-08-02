package com.example.journalapp.core.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.journalapp.R
import com.example.journalapp.feature.entry.EntryCreationScreenEvent
import com.example.journalapp.feature.entry.EntryCreationUiState
import com.example.journalapp.ui.theme.JournalTheme
import com.example.journalapp.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagingEntries(
    navController: NavController,
    onHandleEvent: (EntryCreationScreenEvent) -> Unit,
    uiState: EntryCreationUiState
) {
    // State for managing dialog visibility
    var (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    // State for managing the user input in the dialog
    var entryName by remember { mutableStateOf("") }

    val entries = listOf(
        Pair(stringResource(id = R.string.prompt_1), null),
        Pair(stringResource(id = R.string.prompt_2), null),
        Pair(stringResource(id = R.string.prompt_3), null),
        Pair(stringResource(id = R.string.prompt_4), null),
        Pair(stringResource(id = R.string.prompt_5), null),
        Pair(stringResource(id = R.string.prompt_6), null),
        Pair(stringResource(id = R.string.prompt_7), uiState.emotions),
        Pair(stringResource(id = R.string.prompt_8), uiState.weather),
        Pair(stringResource(id = R.string.prompt_9), uiState.artists)
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
                        EntryDisplay(
                            answer = when (pageIndex) {
                                0 -> uiState.answerOne
                                1 -> uiState.answerTwo
                                2 -> uiState.answerThree
                                3 -> uiState.answerFour
                                4 -> uiState.answerFive
                                5 -> uiState.answerSix
                                else -> ""
                            },//everytime the answer updates it will recompose
                            index = pageIndex,
                            prompt = entries[pageIndex].first,
                            map = entries[pageIndex].second,
                            onHandleEvent,
                        )
                    }
                    //Load + Image Display
                    entries.size -> {
                        if (uiState.prompt.isEmpty()) {//were still loading
                            LoadingScreen()
                        } else {
                            ImageDisplay(string = uiState.prompt)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        //Nav buttons
        if (pageIndex == entries.size) {
            if (uiState.prompt.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = {
                            setShowDialog(true)//triggers dialog that allows user ot name entry
                        },
                    ) {
                        Text(
                            text = "Add to Entries",
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
                                Text(text = "Would you like to name you're entry?")
                            },
                            text = {
                                TextField(
                                    value = entryName,
                                    onValueChange = { entryName = it },
                                    label = { Text("Entry Name") },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary
                                    ),
                                )
                            },
                            //actions for if user does go back
                            confirmButton = {
                                Button(
                                    onClick = {
                                        uiState.entryName = entryName
                                        setShowDialog(false) // Dismiss the dialog
                                        onHandleEvent(EntryCreationScreenEvent.AddToEntries)//todo: update method impl when landing screen is made
                                        navController.navigate("landing")
                                    }
                                ) {
                                    Text("Submit")
                                }
                            },
                            //action if user doesn't wanna go back
                            dismissButton = {
                                Button(
                                    onClick = {
                                        setShowDialog(false)
                                        onHandleEvent(EntryCreationScreenEvent.AddToEntries)//todo: update method impl when landing screen is made
                                        navController.navigate("landing")
                                    }
                                ) {
                                    Text("No")
                                }
                            }
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (pageIndex > 0) pageIndex-- },
                    enabled = pageIndex > 0, // Disable the "Back" button on the first page

                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (pageIndex == entries.size - 1) {
                    Button(
                        onClick = {
                            pageIndex++
                            onHandleEvent(EntryCreationScreenEvent.GenerateClicked)
                        }
                    ) {
                        Text(
                            text = "Generate",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                } else {
                    Button(
                        onClick = { if (pageIndex < entries.size) pageIndex++ },
                        // enabled = pageIndex < entries.size, // Disable the "Forward" button on the last page
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
    }
}


//takes a prompt string, boolean (if frq or mcq), lambda to handle/store the user data
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDisplay(
    answer: String,
    index: Int,
    prompt: String,
    map: Map<String, Boolean>? = mapOf(),
    onHandleEvent: (EntryCreationScreenEvent) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = MaterialTheme.spacing.xLarge,
                vertical = MaterialTheme.spacing.large
            )
    ) {
        //question
        Question(index = index + 1, prompt = prompt)
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.xLarge))
        if (map.isNullOrEmpty()) {
            //Answer Field
            Row {
                TextField(value = answer,
                    onValueChange = { newText ->
                        val words = newText.split(Regex("\\s+"))
                        // Update the answer state directly with the new text
                        if (words.size <= 60) {
                            onHandleEvent(
                                EntryCreationScreenEvent.AnswerEntry(
                                    newText,
                                    index
                                )
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Input must not exceed 60 words",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },//update the uiState w/ the answer
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    label = { Text("Enter your answer (60 words max)") }
                )
            }
        }
        //MCQ
        else {
            MultipleChoice(index, map, onHandleEvent)
        }

    }
}

//takes a list of options and a lambda for when an option is chosen (adds that option to the corresponding answer)
@SuppressLint("SuspiciousIndentation")
@Composable
fun MultipleChoice(
    index: Int,
    map: Map<String, Boolean>,
    onHandleEvent: (EntryCreationScreenEvent) -> Unit
) {
    val originalColor = MaterialTheme.colorScheme.tertiary
    val selectedColor = MaterialTheme.colorScheme.secondary
    val selectedStates = remember { mutableStateMapOf<String, Boolean>().withDefault { false } }
    val scrollState = rememberLazyListState()

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.small)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            items(map.toList()) { entry ->
                val isSelected = selectedStates[entry.first] ?: false
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .padding(vertical = MaterialTheme.spacing.large)
                        .background(
                            color = if (isSelected) selectedColor else originalColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedStates[entry.first] = !isSelected
                            onHandleEvent(
                                EntryCreationScreenEvent.AnswerMcEntry(entry, index)
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = entry.first,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

        }

}

@Composable
fun Question(index: Int, prompt: String) {
    Box(//hold the entire question object
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.33f)//todo: fix this
    ) {
        // This is the larger box to display the question
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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


@Composable
fun PromptDisplay(string: String) {
    AsyncImage(
        model = string,
        contentDescription = null,
    )
}

@Preview
@Composable
fun LoadingScreen() {
    JournalTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Illustration
            // Place your illustration here
            Image(
                painter = painterResource(id = R.drawable.loadstate),
                contentDescription = "LoadImage",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large)) {
                //Load Bar
                IndeterminateLoadingBar()
            }
            Text(
                text = "Generating Image ...",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.large)
            ) //todo: make a stringResource
        }
    }
}

@Composable
fun ImageDisplay(string: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Your Image", style = MaterialTheme.typography.headlineLarge)
        //space
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.xLarge))
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(8.dp)
                )
                .border(
                    4.dp,
                    MaterialTheme.colorScheme.tertiary,
                    RoundedCornerShape(8.dp)
                )
        ) {
            AsyncImage(
                model = string,
                contentDescription = null,
            )
        }

    }
}
