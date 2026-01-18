package com.nereidaro.parksnro.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//La entidad es la tabla de la BDD, en este caso, nuestro parque; tiene ID autogenerado, que es 0 por defecto
@Entity( )
data class Park(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var name: String,
    var desc: String?,
    var phone: String?,
    var webSite: String?,
    var openingTime: String?,
    var closingTime: String?,
    var sports: Boolean?,
    var children: Boolean?,
    var bar: Boolean?,
    var pets: Boolean?,
    var img:String? = ""
): Serializable