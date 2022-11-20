package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["cif", "email"], tableName = "personas_hoteles",
    indices = [Index(value = ["cif"]), Index(value = ["email"])])
data class PersonaHotelCrossRef(
    val email: String,
    val cif: String,
    @ColumnInfo(name = "valoracion")
    val valoracion: String,
    ) {
}