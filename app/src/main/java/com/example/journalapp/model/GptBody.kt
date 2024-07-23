package com.example.journalapp.model




data class GptBody(
    val model: String = "gpt-3.5-turbo",
    val messages: Array<Message> = arrayOf()
) {
    data class Message(
        val role: String = "user",
        val content: String
    )
}