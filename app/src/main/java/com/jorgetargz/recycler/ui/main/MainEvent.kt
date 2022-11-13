package com.jorgetargz.recycler.ui.main

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class MainEvent {
    class AddPersona(val email: String, val nombre: String, val telefono: String, val fnacimiento: String) : MainEvent()
    class ConfirmAddPersona(val persona: Persona) : MainEvent()
    object CleanInputFields : MainEvent()
    object ClearState : MainEvent()
}