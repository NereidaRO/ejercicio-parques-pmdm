package com.nereidaro.parksnro.viewmodel

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nereidaro.parksnro.R
import com.nereidaro.parksnro.db.Park

//esta clase se encarga de gestionar que cada vez que el recycled funcione las cosas estén en orden
class parkViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    val name = itemView.findViewById(R.id.tvParkName) as TextView
    val desc = itemView.findViewById(R.id.tvParkDesc) as TextView

    fun bind(parque: Park,
             eventListenerClick: (Park, View)-> Unit,
             eventListenerLongClick: (Park, View) -> Boolean){
        name.text = parque.name
        desc.text = parque.desc

        itemView.setOnClickListener { eventListenerClick(parque, itemView) }
        itemView.setOnLongClickListener { eventListenerLongClick(parque, itemView) }
    } //de cara a los eventos, le pasaremos dos argumentos de tipo eventListener: clic y clic largo
}