package com.jorgetargz.recycler.data.room.utils

import com.jorgetargz.recycler.data.room.modelo.HotelEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.modelo.Visita

fun Persona.toPersonaEntity(): PersonaEntity {
    return PersonaEntity(this.email, this.nombre, this.fnacimiento!!, this.telefono)
}

fun PersonaEntity.toPersona(): Persona {
    return Persona(this.email, this.nombre, this.fnacimiento, this.telefono)
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
        this.cif,
        this.email,
        this.valoracion
    )
}

fun Visita.toPersonaHotelCrossRef(): PersonaHotelCrossRef {
    return PersonaHotelCrossRef(
        this.cifHotel,
        this.emailPersona,
        this.valoracion
    )
}