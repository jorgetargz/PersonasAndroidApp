package com.jorgetargz.recycler.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgetargz.recycler.data.room.modelo.TarjetaEntity
import com.jorgetargz.recycler.data.room.utils.SQLQueries

@Dao
interface TarjetasDao {

    @Query(SQLQueries.SELECT_TARJETA_BY_NUMERO)
    suspend fun getByNumero(numero: String) : TarjetaEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarjeta: TarjetaEntity)

    @Delete
    suspend fun delete(tarjeta: TarjetaEntity)

    @Delete
    suspend fun deleteAll(tarjetas: List<TarjetaEntity>)

}