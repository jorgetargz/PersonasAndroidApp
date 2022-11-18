package com.jorgetargz.recycler.ui.edit

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class EditEvent {
    class EditPersona(
        val email: String,
        val nombre: String,
        val telefono: String,
        val fnacimiento: String
    ) : EditEvent()

    class UndoEditPersona(val persona: Persona) : EditEvent()
    class LoadPersona(val email: String) : EditEvent()
    object ClearState : EditEvent()
}