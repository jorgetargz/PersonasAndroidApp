package com.jorgetargz.recycler.data.room.utils

import com.jorgetargz.recycler.data.room.modelo.HotelEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef
import com.jorgetargz.recycler.data.room.modelo.TarjetaEntity
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import com.jorgetargz.recycler.domain.modelo.Visita

fun Persona.toPersonaEntity(): PersonaEntity {
    return PersonaEntity(this.email, this.nombre, this.fnacimiento!!, this.telefono)
}

fun PersonaEntity.toPersona(): Persona {
    return Persona(this.email, this.nombre, this.fnacimiento, this.telefono)
}

fun Tarjeta.toTarjetaEntity(): TarjetaEntity {
    return TarjetaEntity(
        this.numero,
        this.fechaCaducidad!!,
        this.cvv,
        this.emailPersona,
        this.nombreTitular
    )
}

fun TarjetaEntity.toTarjeta(): Tarjeta {
    return Tarjeta(
        this.numeroTarjeta,
        this.fechaCaducidad,
        this.cvv,
        this.email,
        this.nombreTitular
    )
}

fun Hotel.toHotelEntity(): HotelEntity {
    return HotelEntity(this.cif, this.nombre, this.estrellas, this.telefono)
}

fun HotelEntity.toHotel(): Hotel {
    return Hotel(
        this.cif,
        this.nombre,
        this.estrellas,
        this.telefono
    )
}

fun PersonaHotelCrossRef.toVisita(): Visita {
    return Visita(
        this.email,
        this.cif,
        this.valoracion
    )
}

fun Visita.toPersonaHotelCrossRef(): PersonaHotelCrossRef {
    return PersonaHotelCrossRef(
        this.emailPersona,
        this.cifHotel,
        this.valoracion
    )
}