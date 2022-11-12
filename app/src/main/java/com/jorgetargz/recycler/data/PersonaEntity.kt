package com.jorgetargz

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "personas")
data class PersonaEntity(
    @PrimaryKey val email: String,
    @ColumnInfo(name = "nombre_persona") val nombre: String,
    @ColumnInfo(name = "fecha_nacimiento") val fnacimiento: LocalDate,
    @ColumnInfo(name = "telefono_personal") val telefono: String,
) {}