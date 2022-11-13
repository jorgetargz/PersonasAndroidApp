package com.jorgetargz.recycler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgetargz.recycler.data.common.Constantes
import com.jorgetargz.recycler.data.modelo.PersonaEntity
import com.jorgetargz.recycler.data.utils.Converters

@Database(entities = [PersonaEntity::class], version = 2)
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
                    Constantes.DATABASE_NAME
                )
                    .createFromAsset(Constantes.DATABASE_PATH)
                    .fallbackToDestructiveMigrationFrom(1)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
