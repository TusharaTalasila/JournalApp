package com.example.journalapp.repositories

import com.example.journalapp.feature.entry.EntryCreationUiState
import com.example.journalapp.network.GptService

class GPTRepository(private val gptService: GptService) {
    fun makePrompt(uiState: EntryCreationUiState): String {
        return """
        GPT-3.5, I need your assistance with the following tasks based on user responses to descriptive questions and multiple-choice questions:\\n\\nDescriptive responses:\\n1. Can you describe a moment from your day that stood out to you the most?: ${uiState.answerOne}\\n2. Did any particular theme or thought recur in your mind throughout the day?: ${uiState.answerTwo}\\n3. Did you have any significant conversations today and how did they make you feel?: ${uiState.answerThree}\\n4. What was a highlight in your day today?: ${uiState.answerFour}\\n5. What was a challenge you faced today?: ${uiState.answerFive}\\n6. If you could summarize your day in 3 words, what would they be?: ${uiState.answerSix}\\n\\nMultiple-choice responses:\\n1. Feelings today: ${
            uiState.emotions.filter { it.value }.keys.joinToString(
                ", "
            )
        }\\n2. Weather: ${uiState.weather.filter { it.value }.keys.joinToString(", ")}\\n3. Artistic style: ${
            uiState.artists.filter { it.value }.keys.joinToString(
                ", "
            )
        }
    """.trimIndent().replace("\n", "\\n")
    }

}