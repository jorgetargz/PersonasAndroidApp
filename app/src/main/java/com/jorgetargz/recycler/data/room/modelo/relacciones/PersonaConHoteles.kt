package com.jorgetargz.recycler.data.room.modelo.relacciones

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jorgetargz.recycler.data.room.common.Constantes
import com.jorgetargz.recycler.data.room.modelo.HotelEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef

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