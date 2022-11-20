package com.jorgetargz.recycler.ui.listado_visitas

import com.jorgetargz.recycler.domain.modelo.Visita

sealed class ListVisitasEvent {
    class DeleteVisita(val cif: String, val email: String) : ListVisitasEvent()
    class UndoDeleteVisita(val visita: Visita) : ListVisitasEvent()
    object LoadVisitas : ListVisitasEvent()
    class LoadVisitasByPersonaEmail(val email: String) : ListVisitasEvent()
    class LoadVisitasByHotelCIF(val cif: String) : ListVisitasEvent()
    object ClearState : ListVisitasEvent()
}