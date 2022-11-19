package com.jorgetargz.recycler.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef

@Dao
interface PersonaHotelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: PersonaHotelCrossRef)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(crossRef: PersonaHotelCrossRef)

    @Delete
    suspend fun delete(crossRef: PersonaHotelCrossRef)

}