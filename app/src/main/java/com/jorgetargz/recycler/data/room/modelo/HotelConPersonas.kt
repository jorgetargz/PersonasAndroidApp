package com.jorgetargz.recycler.data.room.modelo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class HotelConPersonas(
    @Embedded
    val hotel: HotelEntity,
    @Relation(
        parentColumn = "cif",
        entityColumn = "email",
        associateBy = Junction(PersonaHotelCrossRef::class)
    )
    val personas: List<PersonaEntity>
)