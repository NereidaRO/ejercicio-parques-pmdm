package com.nereidaro.parksnro.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nereidaro.parksnro.R
import com.nereidaro.parksnro.model.Park
import com.nereidaro.parksnro.model.Parks

class AdaptadorParques(
    val eventListenerClick: (Park, View) -> Unit,
    val eventListenerLongClick: (Park, View) -> Boolean,
    applicationContext: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        //crear elementos
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        //crear inflater
        val inflater = LayoutInflater.from(parent.context)
        //generar la vista a partir del layout de la tarjeta
        val vista = inflater.inflate(R.layout.park_card, parent, false)
        //retornar la card al view holder
        return parkViewHolder(vista)
    }

    override fun onBindViewHolder(
        //reciclar elementos, enlazar, asignar el valor de un parque concreto
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        //invocamos al bind del viewholder proporcionándole el arrayList
        (holder as parkViewHolder).bind(Parks.parks[position], eventListenerClick, eventListenerLongClick)
    }

    override fun getItemCount(): Int {
        //determinar número de elementos
        //retornar la longitud del array parks del objeto Parks
        return Parks.parks.size
    }

}