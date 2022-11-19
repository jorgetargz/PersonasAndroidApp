package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["email", "cif"], tableName = "personas_hoteles")
data class PersonaHotelCrossRef(
    val cif: String,
    val email: String,
    @ColumnInfo(name = "valoracion")
    val valoracion: String,
    ) {
}