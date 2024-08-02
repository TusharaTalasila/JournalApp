package com.example.journalapp.feature.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalapp.core.components.BaseViewModel
import com.example.journalapp.core.components.BaseViewModelEvent
import com.example.journalapp.getCollectionReferenceForJournalEntries
import com.example.journalapp.model.JournalEntry
import com.example.journalapp.network.ImageServiceImpl
import com.example.journalapp.network.GptServiceImpl
import com.example.journalapp.repositories.GPTRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EntryCreationViewModel() : BaseViewModel, ViewModel() {
    private val imageService = ImageServiceImpl()
    private val gptService = GptServiceImpl()
    private val _uiState = MutableStateFlow(EntryCreationUiState())
    val uiState: StateFlow<EntryCreationUiState> = _uiState.asStateFlow()
    private val gptRepository = GPTRepository(gptService)
    private var generatedPrompt = ""


    //handle event method
    override fun handleEvent(event: BaseViewModelEvent) {
        when (event) {
            is EntryCreationScreenEvent.AnswerEntry -> handleAnswerEntry(event.answer, event.index)
            is EntryCreationScreenEvent.AnswerMcEntry -> handleMcEntry(event.emotion, event.index)
            is EntryCreationScreenEvent.GenerateClicked -> handleSubmitClicked()
            is EntryCreationScreenEvent.AddToEntries -> handleAddToEntries()
        }

    }

    //only update uiState for FRQ
    private fun handleAnswerEntry(answer: String, index: Int) {
        //based on index update the uiState
        when (index) {
            0 -> _uiState.update { it.copy(answerOne = answer) }
            1 -> _uiState.update { it.copy(answerTwo = answer) }
            2 -> _uiState.update { it.copy(answerThree = answer) }
            3 -> _uiState.update { it.copy(answerFour = answer) }
            4 -> _uiState.update { it.copy(answerFive = answer) }
            5 -> _uiState.update { it.copy(answerSix = answer) }
        }

    }

    private fun handleMcEntry(entry: Pair<String, Boolean>, index: Int) {

        val entryList: Map<String, Boolean> = when (index) {
            6 -> uiState.value.emotions
            7 -> uiState.value.weather
            8 -> uiState.value.artists
            else -> mapOf()
        }
        val newEntryList = entryList.toMutableMap()
        newEntryList.compute(entry.first) { _, _ -> !entry.second }
        _uiState.update {
            when (index) {
                6 -> it.copy(emotions = newEntryList)
                7 -> it.copy(weather = newEntryList)
                8 -> it.copy(artists = newEntryList)
                else -> it
            }
        }

    }

    private fun handleSubmitClicked() {
        generatedPrompt = gptRepository.makePrompt(uiState.value)
        //launch coroutine to run asynch functions
        viewModelScope.launch {
            val url = makeAndDisplayImage(generatedPrompt) ?: ""
            _uiState.update { it.copy(prompt = url) }
        }
    }

    private fun handleAddToEntries() {
        addEntryToFirebase()//at this point entry has been made
        _uiState.update { it.copy(prompt = "") }
    }

    private fun addEntryToFirebase() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val entry = JournalEntry(
            userId = currentUser.uid,
            dateCreated = Timestamp.now(),
            imageUrl = _uiState.value.prompt,
            frqAnswers = mapOf(
                "one" to _uiState.value.answerOne,
                "two" to _uiState.value.answerTwo,
                "three" to _uiState.value.answerThree,
                "four" to _uiState.value.answerFour,
                "five" to _uiState.value.answerFive,
                "six" to _uiState.value.answerSix
            ),
            mcAnswers = listOf(
                _uiState.value.emotions,
                _uiState.value.weather,
                _uiState.value.artists
            )
        )
        getCollectionReferenceForJournalEntries()?.add(entry)
            ?.addOnSuccessListener { documentReference ->
                val journalId = documentReference.id
                entry.journalId = journalId
                documentReference.update("journalId", journalId)
            }
            ?.addOnFailureListener { e ->//fix later
                e.message
                //Toast.makeText(context, "Failed to add entry: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private suspend fun makeAndDisplayImage(generatedPrompt: String): String {
        val prompt = gptService.getGptResponse(generatedPrompt)//Todo: uncomment these lines
        val cleanPrompt = prompt.replace(Regex("[^A-Za-z]+"), " ").trim()
        val url = imageService.getImageResponse(cleanPrompt)
        return url ?: ""
    }

}

//sealed class for all types events
sealed class EntryCreationScreenEvent : BaseViewModelEvent() {
    data class AnswerEntry(val answer: String, val index: Int) : EntryCreationScreenEvent()
    data class AnswerMcEntry(val emotion: Pair<String, Boolean>, val index: Int) :
        EntryCreationScreenEvent()

    data object GenerateClicked : EntryCreationScreenEvent()
    data object AddToEntries : EntryCreationScreenEvent()
}

//Data Class for the UI state
data class EntryCreationUiState(
    val answerOne: String = " ",
    val answerTwo: String = " ",
    val answerThree: String = " ",
    val answerFour: String = " ",
    val answerFive: String = " ",
    val answerSix: String = " ",
    val prompt: String = "",
    val emotions: Map<String, Boolean> = mapOf(
        "Happy" to false,
        "Sad" to false,
        "Tired" to false,
        "Scared" to false,
        "Stressed" to false,
        "Calm" to false,
        "Angry" to false,
    ),
    val weather: Map<String, Boolean> = mapOf(
        "Sunny" to false,
        "Cloudy" to false,
        "Rainy" to false,
        "Snowy" to false,
        "Windy" to false,
        "Foggy" to false,
        "Starry night" to false
    ),
    val artists: Map<String, Boolean> = mapOf(
        "Vincent Van Gogh (Post-Impressionism)" to false,
        "Pablo Picasso (Cubism)" to false,
        "Salvador Dalí (Surrealism)" to false,
        "Andy Warhol (Pop Art)" to false,
        "Katsushika Hokusai (Ukiyo-e)" to false,
        "Frida Kahlo (Symbolism/Surrealism)" to false,
        "Jackson Pollock (Abstract Expressionism)" to false
    )
)


