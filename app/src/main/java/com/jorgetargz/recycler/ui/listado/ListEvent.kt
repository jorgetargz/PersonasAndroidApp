package com.jorgetargz.recycler.ui.listado

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class ListEvent {
    class DeletePersona(val email: String) : ListEvent()
    class UndoDeletePersona(val persona: Persona) : ListEvent()
    object LoadPersonas : ListEvent()
    object ClearState : ListEvent()
}