package com.example.journalapp.network


interface GptService {
    suspend fun getGptResponse(question: String) :String //this is the gpt response

}