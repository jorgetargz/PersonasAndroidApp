package com.jorgetargz.recycler.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jorgetargz.PersonaEntity
import com.jorgetargz.recycler.domain.modelo.Persona

@Dao
interface PersonasDao {
    @Query("SELECT * FROM personas")
    suspend fun getAll(): List<PersonaEntity>

    @Query("SELECT * FROM personas WHERE email LIKE :first LIMIT 1")
    suspend fun findByEmail(first: String): PersonaEntity

    @Insert
    suspend fun insert(persona: PersonaEntity)

    @Update
    suspend fun update(persona: PersonaEntity)

    @Delete
    suspend fun delete(persona: PersonaEntity)
}