package com.jorgetargz.recycler.ui.listado_personas

import com.jorgetargz.recycler.domain.modelo.Persona

sealed class ListPersonaEvent {
    class DeletePersona(val email: String) : ListPersonaEvent()
    class UndoDeletePersona(val persona: Persona) : ListPersonaEvent()
    class LoadPersonasByHotelCif(val cifHotel: String) : ListPersonaEvent()
    object LoadPersonas : ListPersonaEvent()
    object ClearState : ListPersonaEvent()
}