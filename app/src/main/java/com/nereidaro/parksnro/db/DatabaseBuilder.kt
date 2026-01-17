package com.nereidaro.parksnro.db

import android.content.Context
import androidx.room.Room

//esto es un singleton: un objeto que solo se instancia una vez
object DatabaseBuilder {
    private var INSTANCE: ParksDB? = null

    fun getInstance(context: Context): ParksDB{
        if (INSTANCE == null){
            synchronized(ParksDB::class){
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!! //Significa "confío en que ya no es null"
    }
    //Constructor privado (solo se usa arriba si la instancia es nula)
    private fun buildRoomDB(contexto: Context) = Room.databaseBuilder(
        contexto.applicationContext,
        ParksDB::class.java,
        "parks-db"
    ).build()
}