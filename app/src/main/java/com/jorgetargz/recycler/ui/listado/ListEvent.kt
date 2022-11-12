package com.jorgetargz.recycler.ui.listado

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class ListEvent {
    class OnDeletePersona(val email: String) : ListEvent()
    class OnUndoDeletePersona(val persona: Persona) : ListEvent()
    object LoadPersonas : ListEvent()
    object OnClearState : ListEvent()
}