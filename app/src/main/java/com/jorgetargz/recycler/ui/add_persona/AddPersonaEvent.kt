package com.jorgetargz.recycler.ui.add_persona

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class AddPersonaEvent {
    class AddPersona(
        val email: String,
        val nombre: String,
        val telefono: String,
        val fnacimiento: String,
    ) : AddPersonaEvent()

    class UndoAddPersona(val persona: Persona) : AddPersonaEvent()
    object CleanInputFields : AddPersonaEvent()
    object ClearState : AddPersonaEvent()
}