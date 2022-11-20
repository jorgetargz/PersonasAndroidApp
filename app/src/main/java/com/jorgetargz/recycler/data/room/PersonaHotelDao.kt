package com.jorgetargz.recycler.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef
import com.jorgetargz.recycler.data.room.utils.SQLQueries

@Dao
interface PersonaHotelDao {

    @Query(SQLQueries.SELECT_ALL_VISITAS)
    suspend fun getAll(): List<PersonaHotelCrossRef>

    @Query(SQLQueries.SELECT_VISITAS_BY_EMAIL)
    suspend fun findByEmail(email: String): List<PersonaHotelCrossRef>

    @Query(SQLQueries.SELECT_VISITAS_BY_CIF)
    suspend fun findByCif(cif: String): List<PersonaHotelCrossRef>

    @Query(SQLQueries.SELECT_VISITAS_BY_EMAIL_AND_CIF)
    suspend fun findByEmailAndCif(email: String, cif: String): PersonaHotelCrossRef?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: PersonaHotelCrossRef)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(crossRef: PersonaHotelCrossRef)

    @Delete
    suspend fun delete(crossRef: PersonaHotelCrossRef)

}