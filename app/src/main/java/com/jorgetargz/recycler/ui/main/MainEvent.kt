package com.jorgetargz.recycler.ui.main

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class MainEvent {
    class OnAddPersona(val email: String, val nombre: String, val telefono: String, val fnacimiento: String) : MainEvent()
    class OnConfirmAddPersona(val persona: Persona) : MainEvent()
    object OnCleanFields : MainEvent()
    object OnClearState : MainEvent()
}