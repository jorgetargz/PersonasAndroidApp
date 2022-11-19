package com.jorgetargz.recycler.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableLiveData(
        MainState(null)
    )

    val uiState: LiveData<MainState> get() = _uiState


    private fun clearState() {
        _uiState.value =
            _uiState.value?.copy(mensaje = null)
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ClearState -> clearState()
        }
    }
}