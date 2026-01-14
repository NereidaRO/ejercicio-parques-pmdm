package com.nereidaro.parksnro.repository

import android.content.Context
import com.nereidaro.parksnro.R
import com.nereidaro.parksnro.model.Park
import com.nereidaro.parksnro.model.Parks

class ParkRepository private constructor(private var context: Context) {
    init{
        Parks.populate(context, R.raw.parks)}
    //Implementación del patrón Singleton
    companion object{
        private var INSTANCE: ParkRepository? = null
        fun getInstance(context: Context): ParkRepository{
            if(INSTANCE == null){
                INSTANCE = ParkRepository(context)
            }
            return INSTANCE!!
        }
    }

    //Métodos de la API del Repositorio
    fun getParks() = Parks.parks;
    fun getNumParks() = Parks.parks.size;
    fun removePark (p: Park) = Parks.remove(p);
}