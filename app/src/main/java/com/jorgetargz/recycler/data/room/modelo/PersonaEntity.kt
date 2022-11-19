package com.jorgetargz.recycler.data.room.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "personas")
data class PersonaEntity(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    @ColumnInfo(name = "nombre_persona")
    val nombre: String,
    @ColumnInfo(name = "fecha_nacimiento")
    val fnacimiento: LocalDate,
    @ColumnInfo(name = "telefono_personal")
    val telefono: String,
)