package com.example.journalapp.network

interface ImageService{
    suspend fun getImageResponse(question: String):String?
}