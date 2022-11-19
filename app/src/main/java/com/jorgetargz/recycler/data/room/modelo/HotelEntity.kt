package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hoteles")
data class HotelEntity(
    @PrimaryKey(autoGenerate = false)
    val cif: String,
    @ColumnInfo(name = "nombre_hotel")
    val nombre: String,
    @ColumnInfo(name = "estrellas")
    var estrellas: Int,
    @ColumnInfo(name = "telefono_recepcion")
    val telefono: String,
)