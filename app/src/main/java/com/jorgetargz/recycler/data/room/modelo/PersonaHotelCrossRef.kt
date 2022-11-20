package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.jorgetargz.recycler.data.room.common.Constantes

@Entity(primaryKeys = [Constantes.COLUMN_EMAIL, Constantes.COLUMN_CIF],
    tableName = Constantes.TABLE_PERSONAS_HOTELES,
    indices = [Index(value = [Constantes.COLUMN_CIF]), Index(value = [Constantes.COLUMN_EMAIL])])
data class PersonaHotelCrossRef(
    val email: String,
    val cif: String,
    @ColumnInfo(name = Constantes.COLUMN_VALORACION)
    val valoracion: String,
    ) {
}