package com.example.journalapp.feature.entries

import android.icu.text.StringSearch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalapp.core.components.BaseViewModel
import com.example.journalapp.core.components.BaseViewModelEvent
import com.example.journalapp.model.JournalEntry
import com.example.journalapp.network.DBServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LandingViewModel : BaseViewModel, ViewModel() {
    private val dbService = DBServiceImpl()
    private val user = FirebaseAuth.getInstance().currentUser
    private val _uiState = MutableStateFlow(LandingScreenUiState())
    val uiState: StateFlow<LandingScreenUiState> = _uiState.asStateFlow()

    init {
        fetchJournalEntries()
    }

    override fun handleEvent(event: BaseViewModelEvent) {
        when (event) {
            is LandingScreenEvent.SearchQueryChanged -> handleSearchQueryChanged(event.query)
            is LandingScreenEvent.SearchCleared -> handleSearchCleared()
            is LandingScreenEvent.EntryCardClicked -> handleEntryCardClicked(event.selectedEntry)
            is LandingScreenEvent.EntryCardReset -> handleEntryCardReset()
            is LandingScreenEvent.EntryCardFlipped -> handleEntryCardFlipped()
        }
    }

    fun fetchJournalEntries() {
        viewModelScope.launch {
            var entries: List<JournalEntry>? = listOf()
            while (entries.isNullOrEmpty()) {
                entries = user?.let { dbService.getJournalEntries(it.uid) }
            }
            _uiState.update { it.copy(journalEntries = entries) }
        }
    }

    private fun handleSearchQueryChanged(query: String) {
        // update the uiState with the new query
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun handleSearchCleared() {
        // update the uiState with empty string
        _uiState.update { it.copy(searchQuery = "") }
    }

    private fun handleEntryCardClicked(selectedEntry: JournalEntry?) {
        //update uiState
        selectedEntry?.let{
            _uiState.update { it.copy(selectedEntry = selectedEntry) }
        }
    }

    private fun handleEntryCardReset(){
        _uiState.update { it.copy(selectedEntry = null, isFlipped = false) }
    }

    private fun handleEntryCardFlipped(){
        val switch = !uiState.value.isFlipped
        _uiState.update { it.copy(isFlipped = switch) }
    }


}

sealed class LandingScreenEvent : BaseViewModelEvent() {
    data class SearchQueryChanged(val query: String) : LandingScreenEvent()
    data object SearchCleared : LandingScreenEvent()
    data class EntryCardClicked(val selectedEntry: JournalEntry) : LandingScreenEvent()
    data object EntryCardReset: LandingScreenEvent()
    data object EntryCardFlipped : LandingScreenEvent()

}

data class LandingScreenUiState(
    val isFlipped: Boolean = false,
    var selectedEntry: JournalEntry? = null,
    val searchQuery: String = "",
    val journalEntries: List<JournalEntry> = listOf()
) {
    val filteredEntries = journalEntries.filterWithQuery(searchQuery)
    private fun List<JournalEntry>.filterWithQuery(query: String): List<JournalEntry> {
        val filteredList = this.filter { item ->
            if (query.isEmpty()) {
                true
            } else {
                item.entryName.contains(
                    query,
                    ignoreCase = true,
                )
            }
        }
        return filteredList
    }
}