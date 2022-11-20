package com.jorgetargz.recycler.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef

@Dao
interface PersonaHotelDao {

    @Query("SELECT * FROM personas_hoteles")
    suspend fun getAll(): List<PersonaHotelCrossRef>

    @Query("SELECT * FROM personas_hoteles WHERE email = :email")
    suspend fun findByEmail(email: String): List<PersonaHotelCrossRef>

    @Query("SELECT * FROM personas_hoteles WHERE cif = :cif")
    suspend fun findByCif(cif: String): List<PersonaHotelCrossRef>

    @Query("SELECT * FROM personas_hoteles WHERE email = :email AND cif = :cif LIMIT 1")
    suspend fun findByEmailAndCif(email: String, cif: String): PersonaHotelCrossRef?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: PersonaHotelCrossRef)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(crossRef: PersonaHotelCrossRef)

    @Delete
    suspend fun delete(crossRef: PersonaHotelCrossRef)

}