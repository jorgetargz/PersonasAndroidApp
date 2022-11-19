package com.jorgetargz.recycler.ui.edit_persona

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class EditPersonaEvent {
    class EditPersona(
        val email: String,
        val nombre: String,
        val telefono: String,
        val fnacimiento: String
    ) : EditPersonaEvent()

    class UndoEditPersona(val persona: Persona) : EditPersonaEvent()
    class LoadPersona(val email: String) : EditPersonaEvent()
    object ClearState : EditPersonaEvent()
}