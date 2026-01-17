package com.nereidaro.parksnro.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nereidaro.parksnro.db.Park
import com.nereidaro.parksnro.db.ParkDAO

//Esta es la descripción de la base de datos
@Database(
    entities = [Park::class],
    version = 1
)
abstract class ParksDB : RoomDatabase() {

    abstract fun parkDao(): ParkDAO
}