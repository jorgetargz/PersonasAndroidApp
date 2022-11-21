package com.jorgetargz.recycler.data.room.modelo.relacciones

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jorgetargz.recycler.data.room.common.Constantes
import com.jorgetargz.recycler.data.room.modelo.HotelEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef

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