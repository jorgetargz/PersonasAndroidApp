package com.jorgetargz.recycler.ui.listado_tarjetas

import com.jorgetargz.recycler.domain.modelo.Tarjeta

sealed class ListTarjetasEvent {
    class DeleteTarjeta(val numeroTarjeta: String) : ListTarjetasEvent()
    class UndoDeleteTarjeta(val tarjeta: Tarjeta) : ListTarjetasEvent()
    class LoadTarjetasByEmail(val email: String) : ListTarjetasEvent()
    object ClearState : ListTarjetasEvent()
}