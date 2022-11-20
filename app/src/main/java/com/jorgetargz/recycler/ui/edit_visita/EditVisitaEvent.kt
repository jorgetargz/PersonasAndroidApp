package com.jorgetargz.recycler.ui.edit_visita

import com.jorgetargz.recycler.domain.modelo.Visita

sealed class EditVisitaEvent {
    class EditVisita(
        val cif: String,
        val email: String,
        val valoracion: String,
    ) : EditVisitaEvent()

    class UndoEditVisita(val visita: Visita) : EditVisitaEvent()
    class LoadVisita(val cif: String, val email: String) : EditVisitaEvent()
    object ClearState : EditVisitaEvent()
}