package com.jorgetargz.recycler.ui.add_tarjeta

import com.jorgetargz.recycler.domain.modelo.Tarjeta

sealed class AddTarjetaEvent {
    class AddTarjeta(
        val email: String,
        val titular: String,
        val numero: String,
        val fechaCaducidad: String,
        val cvv: String,
    ) : AddTarjetaEvent()

    class UndoAddTarjeta(val tarjeta: Tarjeta) : AddTarjetaEvent()
    object CleanInputFields : AddTarjetaEvent()
    object ClearState : AddTarjetaEvent()
}