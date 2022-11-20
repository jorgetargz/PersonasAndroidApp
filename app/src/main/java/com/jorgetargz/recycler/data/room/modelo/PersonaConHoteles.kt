package com.jorgetargz.recycler.data.room.modelo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jorgetargz.recycler.data.room.common.Constantes

data class PersonaConHoteles(
    @Embedded
    val person: PersonaEntity,
    @Relation(
        parentColumn = Constantes.COLUMN_EMAIL,
        entityColumn = Constantes.COLUMN_CIF,
        associateBy = Junction(PersonaHotelCrossRef::class)
    )
    val hotels: List<HotelEntity>
)