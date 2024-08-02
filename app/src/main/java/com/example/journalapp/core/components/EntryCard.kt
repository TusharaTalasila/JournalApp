package com.example.journalapp.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.example.journalapp.ui.theme.spacing

@Composable
fun EntryCard(imageUrl: String, date: String){
    val imagePainter = rememberImagePainter(data = imageUrl)
    ImageCard(imagePainter = imagePainter, date = date)
}

@Composable
fun ImageCard(imagePainter: Painter, date: String){
    Card(
       // elevation = MaterialTheme.spacing.xSmall// Set the elevation of the card
    ) {
        Column {
            Image(
                painter = imagePainter,
                contentDescription = "",
                contentScale = ContentScale.Crop // Adjust the scaling of the image if necessary
            )
            Text(
                text = date,
                modifier = Modifier.padding(MaterialTheme.spacing.small) // Add padding around the text
            )
        }
    }
}