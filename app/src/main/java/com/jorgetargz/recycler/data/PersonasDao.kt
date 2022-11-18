package com.jorgetargz.recycler.data

import androidx.room.*
import com.jorgetargz.recycler.data.modelo.PersonaEntity
import com.jorgetargz.recycler.data.utils.SQLQueries

@Dao
interface PersonasDao {

    @Query(SQLQueries.SELECT_ALL_PERSONAS)
    suspend fun getAll(): List<PersonaEntity>

    @Query(SQLQueries.SELECT_PERSONA_BY_EMAIL)
    suspend fun findByEmail(first: String): PersonaEntity

    @Insert
    suspend fun insert(persona: PersonaEntity)

    @Update
    suspend fun update(persona: PersonaEntity)

    @Delete
    suspend fun delete(persona: PersonaEntity)

}