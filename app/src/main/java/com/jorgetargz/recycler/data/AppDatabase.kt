package com.jorgetargz.recycler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgetargz.PersonaEntity
import com.jorgetargz.recycler.domain.modelo.Persona

@Database(entities = [PersonaEntity::class], version = 1)
@TypeConverters(Converters::class)
    abstract class AppDatabase : RoomDatabase() {
    abstract fun personasDao(): PersonasDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personas_database"
                )
                    .createFromAsset("database/personas.db")
                    .fallbackToDestructiveMigrationFrom(4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
