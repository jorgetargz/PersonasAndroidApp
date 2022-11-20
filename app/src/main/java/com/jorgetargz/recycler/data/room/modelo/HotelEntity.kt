package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jorgetargz.recycler.data.room.common.Constantes

@Entity(tableName = Constantes.TABLE_HOTELES)
data class HotelEntity(
    @PrimaryKey(autoGenerate = false)
    val cif: String,
    @ColumnInfo(name = Constantes.COLUMN_NOMBRE_HOTEL)
    val nombre: String,
    @ColumnInfo(name = Constantes.COLUMN_ESTRELLAS)
    var estrellas: Int,
    @ColumnInfo(name = Constantes.COLUMN_TELEFONO_RECEPCION)
    val telefono: String,
)