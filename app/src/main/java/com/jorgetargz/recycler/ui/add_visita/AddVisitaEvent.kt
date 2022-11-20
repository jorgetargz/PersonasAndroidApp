package com.jorgetargz.recycler.ui.add_visita

import com.jorgetargz.recycler.domain.modelo.Visita

sealed class AddVisitaEvent {
    class AddVisita(
        val cif: String,
        val email: String,
        val valoracion: String,
    ) : AddVisitaEvent()

    class UndoAddVisita(val visita: Visita) : AddVisitaEvent()
    object CleanInputFields : AddVisitaEvent()
    object ClearState : AddVisitaEvent()
    object LoadDropdownLists : AddVisitaEvent()
}