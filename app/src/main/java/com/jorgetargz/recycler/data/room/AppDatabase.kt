package com.jorgetargz.recycler.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgetargz.recycler.data.room.modelo.HotelEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaEntity
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef
import com.jorgetargz.recycler.data.room.utils.Converters

@Database(
    entities = [PersonaEntity::class, HotelEntity::class, PersonaHotelCrossRef::class],
    version = 2, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personasDao(): PersonasDao
    abstract fun hotelesDao(): HotelesDao
    abstract fun personaHotelDao(): PersonaHotelDao

}
