package com.example.journalapp.network

import com.example.journalapp.model.GptBody


interface GptService {
    suspend fun getGptResponse(question: String) :String //this is the gpt response

}