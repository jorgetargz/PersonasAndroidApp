package com.jorgetargz.recycler.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef

@Dao
interface PersonaHotelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: PersonaHotelCrossRef)

    @Delete
    suspend fun delete(crossRef: PersonaHotelCrossRef)

}