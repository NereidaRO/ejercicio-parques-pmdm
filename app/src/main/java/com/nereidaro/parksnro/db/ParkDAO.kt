package com.nereidaro.parksnro.db

import androidx.lifecycle.LiveData
import androidx.room.*

/* Clase de Acceso a Datos */

@Dao
interface ParkDAO {

    // Obtener la lista de parques
    @Query("SELECT * FROM Park")
    fun getAll(): LiveData<List<Park>>

    // Añadir un parque
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPark(park: Park)

    // Actualizar un parque
    @Update
    suspend fun updatePark(park: Park)

    // Eliminar un parque
    @Delete
    suspend fun deletePark(park: Park): Int
}