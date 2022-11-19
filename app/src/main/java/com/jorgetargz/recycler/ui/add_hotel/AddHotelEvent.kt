package com.jorgetargz.recycler.ui.add_hotel

import com.jorgetargz.recycler.domain.modelo.Hotel

sealed class AddHotelEvent {
    class AddHotel(
        val cif: String,
        val nombre: String,
        val telefono: String,
        val estrellas: String,
    ) : AddHotelEvent()

    class UndoAddHotel(val hotel: Hotel) : AddHotelEvent()
    object CleanInputFields : AddHotelEvent()
    object ClearState : AddHotelEvent()
}