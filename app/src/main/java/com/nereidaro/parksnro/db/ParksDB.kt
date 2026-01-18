package com.nereidaro.parksnro.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nereidaro.parksnro.db.Park
import com.nereidaro.parksnro.db.ParkDAO

//Esta es la descripción de la base de datos
@Database(
    entities = [Park::class],
    version = 2
)

abstract class ParksDB : RoomDatabase() {

    abstract fun parkDao(): ParkDAO
}
val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(database: SupportSQLiteDatabase){
        database.execSQL("ALTER TABLE Park ADD COLUMN tmpUri TEXT")
    }
}