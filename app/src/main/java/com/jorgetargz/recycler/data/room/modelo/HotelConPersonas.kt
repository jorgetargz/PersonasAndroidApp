package com.jorgetargz.recycler.data.room.modelo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jorgetargz.recycler.data.room.common.Constantes

class HotelConPersonas(
    @Embedded
    val hotel: HotelEntity,
    @Relation(
        parentColumn = Constantes.COLUMN_CIF,
        entityColumn = Constantes.COLUMN_EMAIL,
        associateBy = Junction(PersonaHotelCrossRef::class)
    )
    val personas: List<PersonaEntity>
)