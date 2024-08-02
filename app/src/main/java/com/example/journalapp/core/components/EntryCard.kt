package com.example.journalapp.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.example.journalapp.model.JournalEntry
import com.example.journalapp.ui.theme.spacing
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EntryCard(modifier: Modifier, imageUrl: String, name: String, date: Timestamp?) {
    val imagePainter = rememberImagePainter(data = imageUrl)
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    val dateString = date?.toDate()?.let {
        formatter.format(it)
    } ?: ""
    ImageCard(modifier = modifier, imagePainter = imagePainter, name, date = dateString)
}

@Composable
fun ImageCard(modifier: Modifier, imagePainter: Painter, name: String, date: String) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.secondary)) {
            // Use weight to allocate a portion of the space to the image, allowing room for text
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop, // Adjust the scaling of the image if necessary
                modifier = Modifier
                    .weight(0.70f) // Adjust this weight as needed to make the image smaller
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.small) // Add padding around the text
                    .weight(0.3f) // Allocate the remaining space to the text
                    .fillMaxWidth()
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun EntryList(entries: List<JournalEntry>) {
    // Assuming there is a data class named Entry with imageUrl and dateCreated fields.
    Column(Modifier.padding(MaterialTheme.spacing.medium)) {
        if (entries.isNotEmpty()) {
            entries.chunked(2).forEach { chunk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.small), // Adjust vertical padding if necessary
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (entry in chunk) {
                        EntryCard(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(2.45f / 3f), // This maintains the aspect ratio of 1/3 width to 1/4 height
                            imageUrl = entry.imageUrl,
                            name = entry.entryName,
                            date = entry.dateCreated
                        )
                        if (chunk.size == 2 && entry != chunk.last()) {
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        }
                    }
                    if (chunk.size == 1) {
                        Spacer(modifier = Modifier.weight(1f)) // This ensures the single card is centered
                    }
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }
        }
    }
}

