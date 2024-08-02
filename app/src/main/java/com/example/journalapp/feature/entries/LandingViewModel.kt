package com.example.journalapp.feature.entries

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

    init{
        fetchJournalEntries()
    }
    override fun handleEvent(event: BaseViewModelEvent) {
        when (event) {

        }
    }

    fun fetchJournalEntries(){
        viewModelScope.launch {
            val entries = user?.let { dbService.getJournalEntries(it.uid) }
            _uiState.update { it.copy(journalEntries = entries?: listOf()) }
        }
    }

}

sealed class LandingScreenEvent : BaseViewModelEvent() {
    data class SearchEntries(val query: String) : BaseViewModelEvent()
    data object EntryCardClicked : BaseViewModelEvent()
    data object PlusClicked : BaseViewModelEvent()
}

data class LandingScreenUiState(
    val journalEntries: List<JournalEntry> = listOf()

)