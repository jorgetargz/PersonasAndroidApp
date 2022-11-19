package com.jorgetargz.recycler.data.room

import androidx.room.*
import com.jorgetargz.recycler.data.room.modelo.HotelConPersonas
import com.jorgetargz.recycler.data.room.modelo.HotelEntity
import com.jorgetargz.recycler.data.room.utils.SQLQueries

@Dao
interface HotelesDao {

    @Query(SQLQueries.SELET_ALL_HOTELS)
    suspend fun getAll(): List<HotelEntity>

    @Transaction
    @Query(SQLQueries.SELECT_HOTEL_BY_CIF)
    suspend fun getPersonasOfHotel(cif: String): List<HotelConPersonas>

    @Query(SQLQueries.SELECT_HOTEL_BY_CIF)
    suspend fun findByCIF(cif: String): HotelEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hotel: HotelEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(hotel: HotelEntity)

    @Delete
    suspend fun delete(hotel: HotelEntity)
}