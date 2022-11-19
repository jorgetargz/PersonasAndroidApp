package com.jorgetargz.recycler.data.room.modelo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PersonaConHoteles(
    @Embedded
    val person: PersonaEntity,
    @Relation(
        parentColumn = "email",
        entityColumn = "CIF",
        associateBy = Junction(PersonaHotelCrossRef::class)
    )
    val hotels: List<HotelEntity>
)