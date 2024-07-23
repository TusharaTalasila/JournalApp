package com.example.journalapp.core.components

interface BaseViewModel {
    fun handleEvent(event: BaseViewModelEvent)
}

abstract class BaseViewModelEvent()

abstract class BaseViewModelUiState(
    open val trace: String,
)