package com.jorgetargz.recycler.data.room

import androidx.room.*
import com.jorgetargz.recycler.data.room.modelo.relacciones.PersonaConHoteles
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.relacciones.PersonaConTarjetas
import com.jorgetargz.recycler.data.room.utils.SQLQueries

@Dao
interface PersonasDao {

    @Query(SQLQueries.SELECT_ALL_PERSONAS)
    suspend fun getAll(): List<PersonaEntity>

    @Transaction
    @Query(SQLQueries.SELECT_PERSONA_BY_EMAIL)
    suspend fun getHotelsOfPersona(email: String): List<PersonaConHoteles>

    @Transaction
    @Query(SQLQueries.SELECT_PERSONA_BY_EMAIL)
    suspend fun getTarjetasOfPersona(email: String): List<PersonaConTarjetas>

    @Query(SQLQueries.SELECT_PERSONA_BY_EMAIL)
    suspend fun findByEmail(email: String): PersonaEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(persona: PersonaEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(persona: PersonaEntity)

    @Delete
    suspend fun delete(persona: PersonaEntity)

}