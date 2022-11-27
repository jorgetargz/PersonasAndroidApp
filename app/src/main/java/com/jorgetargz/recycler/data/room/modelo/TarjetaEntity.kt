package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jorgetargz.recycler.data.room.common.Constantes
import java.time.LocalDate

@Entity(tableName = Constantes.TABLE_TARJETAS,
    foreignKeys = [ForeignKey(
        entity = PersonaEntity::class,
        parentColumns = ["email"],
        childColumns = ["email"])])
data class TarjetaEntity(
    @PrimaryKey(autoGenerate = false)
    val numeroTarjeta: String,
    @ColumnInfo(name = Constantes.COLUMN_FECHA_CADUCIDAD)
    val fechaCaducidad: LocalDate,
    @ColumnInfo(name = Constantes.COLUMN_CVV)
    val cvv: Int,
    @ColumnInfo(name = Constantes.COLUMN_EMAIL)
    val email: String,
    @ColumnInfo(name = Constantes.COLUMN_NOMBRE_TITULAR)
    val nombreTitular: String,

)