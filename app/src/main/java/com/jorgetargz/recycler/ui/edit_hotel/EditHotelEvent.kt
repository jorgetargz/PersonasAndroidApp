package com.jorgetargz.recycler.ui.edit_hotel

import com.jorgetargz.recycler.domain.modelo.Hotel

sealed class EditHotelEvent {
    class EditHotel(
        val cif: String,
        val nombre: String,
        val telefono: String,
        val estrellas: String
    ) : EditHotelEvent()

    class UndoEditHotel(val hotel: Hotel) : EditHotelEvent()
    class LoadHotel(val cif: String) : EditHotelEvent()
    object ClearState : EditHotelEvent()
}