package com.nereidaro.parksnro.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nereidaro.parksnro.model.Park
import com.nereidaro.parksnro.repository.ParkRepository

// Definimos el ViewModel como AndroidViewModel,
// para poder acceder al contexto
class FirstFragmentViewModel(application: Application) :
    AndroidViewModel(application) {
    //Coger la lista inicial del repositorio
    init {
        ParkRepository.getInstance(getApplication<Application>().applicationContext)
    }
    // Definición de atributos

    // Definimos los atributos LiveData para gestionar los clics,
    // de manera que puedan ser observados por un observer en el fragmento
    val parkLongClicked: MutableLiveData<Park> by lazy {
        MutableLiveData<Park>()
    }

    val parkClicked: MutableLiveData<Park> by lazy {
        MutableLiveData<Park>()
    }

    // LiveData para el adaptador mediante un atributo de soporte privado:
    // El adaptador del RecyclerView también será un LiveData para
    // poder ser observable desde la vista.
    private val _adaptador = MutableLiveData<AdaptadorParques>().apply {

        // Mediante value (setValue) establecemos el valor que
        // contendrá el MutableLiveData de tipo AdaptadorParques.
        // Aquí proporcionamos también los callbacks que se deben
        // invocar cuando se produzcan clics.
        value = AdaptadorParques(
            { park: Park, v: View -> parkClickedManager(park, v) },
            { park: Park, v: View -> parkLongClickedManager(park, v) },
            getApplication<Application>().applicationContext
            // Esto lo podemos hacer gracias a extender de AndroidViewModel
        )
    }

    // Definimos la propiedad que podremos leer desde la vista
    // (_adapter es un atributo de soporte privado)
    val adaptador: MutableLiveData<AdaptadorParques> = _adaptador

    // Definición de métodos

    // Definimos las funciones de callback para la gestión de eventos
    // El evento lo gestionará el FirstFragment, pero será avisado
    // por el ViewModel cuando se produzcan los clics sobre cada elemento.
    private fun parkClickedManager(park: Park, v: View) {
        // Para establecer el valor del liveData utilizamos value
        parkClicked.value = park
    }

    private fun parkLongClickedManager(park: Park, v: View): Boolean {
        // Para establecer el valor del liveData utilizamos value
        parkLongClicked.value = park
        return true
    }

    // Método para eliminar un parque de la lista.
    // Se modificará el modelo a través del repositorio, y
    // se notificará al Adaptador para que se refresque.
    fun removePark(park: Park): Int {

        val index = ParkRepository
            .getInstance(getApplication<Application>().applicationContext)
            .removePark(park)

        adaptador.value?.notifyItemRemoved(index)
        return index
    }
}