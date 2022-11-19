package com.jorgetargz.recycler.ui.listado_hoteles

import com.jorgetargz.recycler.domain.modelo.Hotel

sealed class ListHotelesEvent {
    class DeleteHotel(val cif: String) : ListHotelesEvent()
    class UndoDeleteHotel(val hotel: Hotel) : ListHotelesEvent()
    object LoadHoteles : ListHotelesEvent()
    class LoadHotelesByPersonaEmail(val email: String) : ListHotelesEvent()
    object ClearState : ListHotelesEvent()
}