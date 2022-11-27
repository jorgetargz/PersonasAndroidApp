package com.jorgetargz.recycler.ui.listado_personas

import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.modelo.Tarjeta

sealed class ListPersonaEvent {
    class DeletePersona(val email: String) : ListPersonaEvent()
    class UndoDeletePersona(val persona: Persona, val tarjetas: List<Tarjeta>?) : ListPersonaEvent()
    class LoadPersonasByHotelCif(val cifHotel: String) : ListPersonaEvent()
    object LoadPersonas : ListPersonaEvent()
    object ClearState : ListPersonaEvent()
}