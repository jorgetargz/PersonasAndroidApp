package com.jorgetargz.recycler.data.room.modelo

import androidx.room.Entity

@Entity(primaryKeys = ["email", "cif"], tableName = "personas_hoteles")
data class PersonaHotelCrossRef(
    val cif: String,
    val email: String,
) {
}