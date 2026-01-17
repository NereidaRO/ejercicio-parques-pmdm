package com.nereidaro.parksnro.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.nereidaro.parksnro.db.DatabaseBuilder
import com.nereidaro.parksnro.db.Park

class ParkRepository private constructor(
    private var context: Context
) {

    companion object {
        private var INSTANCE: ParkRepository? = null

        fun getInstance(context: Context): ParkRepository {
            if (INSTANCE == null) {
                INSTANCE = ParkRepository(context)
            }
            return INSTANCE!!
        }
    }

    // Métodos que ofrece la API del repositorio

    fun getParks(): LiveData<List<Park>> {
        return DatabaseBuilder
            .getInstance(context)
            .parkDao()
            .getAll()
    }

    suspend fun removePark(park: Park): Int =
        DatabaseBuilder
            .getInstance(context)
            .parkDao()
            .deletePark(park)

    suspend fun updatePark(park: Park) =
        DatabaseBuilder
            .getInstance(context)
            .parkDao()
            .updatePark(park)

    suspend fun addPark(park: Park) =
        DatabaseBuilder
            .getInstance(context)
            .parkDao()
            .addPark(park)
}