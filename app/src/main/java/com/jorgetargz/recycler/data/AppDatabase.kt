package com.jorgetargz.recycler.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgetargz.recycler.data.modelo.PersonaEntity
import com.jorgetargz.recycler.data.utils.Converters

@Database(entities = [PersonaEntity::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personasDao(): PersonasDao

}
