package com.jorgetargz.recycler.data.room.modelo.relacciones

import androidx.room.Embedded
import androidx.room.Relation
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.TarjetaEntity

data class PersonaConTarjetas(
    @Embedded
    val person: PersonaEntity,
    @Relation(
        parentColumn = "email",
        entityColumn = "email"
    )
    val tarjetas: List<TarjetaEntity>
)