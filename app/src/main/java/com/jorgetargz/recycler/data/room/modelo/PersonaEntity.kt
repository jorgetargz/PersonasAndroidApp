package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jorgetargz.recycler.data.room.common.Constantes
import java.time.LocalDate

@Entity(tableName = Constantes.TABLE_PERSONAS)
data class PersonaEntity(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    @ColumnInfo(name = Constantes.COLUMN_NOMBRE_PERSONA)
    val nombre: String,
    @ColumnInfo(name = Constantes.COLUMN_FNACIMIENTO)
    val fnacimiento: LocalDate,
    @ColumnInfo(name = Constantes.COLUMN_TELEFONO_PERSONA)
    val telefono: String,
)