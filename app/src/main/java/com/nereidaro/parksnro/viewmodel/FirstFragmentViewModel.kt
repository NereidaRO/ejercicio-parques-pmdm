package com.nereidaro.parksnro.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nereidaro.parksnro.db.Park
import com.nereidaro.parksnro.repository.ParkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirstFragmentViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio
    private val repository: ParkRepository by lazy {
        ParkRepository.getInstance(application.applicationContext)
    }
    //Parque en edición
    private var _currentPark = MutableLiveData<Park?>()
    val currentPark: LiveData<Park?> = _currentPark

    // Lista
    val parkList: LiveData<List<Park>> by lazy {
        repository.getParks()
    }

    // Gestionar clics
    val parkLongClicked: MutableLiveData<Park> by lazy { MutableLiveData<Park>() }
    val parkClicked: MutableLiveData<Park> by lazy { MutableLiveData<Park>() }

    // LiveData en el adaptador
    private val _adaptador = MutableLiveData<AdaptadorParques>().apply {
        value = AdaptadorParques(
            { park: Park, v: View -> parkClickedManager(park, v) },
            { park: Park, v: View -> parkLongClickedManager(park, v) },
            getApplication<Application>().applicationContext
        )
    }
    val adaptador: MutableLiveData<AdaptadorParques> = _adaptador

    val deletedPos: MutableLiveData<Int> by lazy { MutableLiveData() }
    val parkSaved: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val parkUpdated: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    // Manejadores eventos
    private fun parkClickedManager(park: Park, v: View) {
        parkClicked.value = park
    }

    private fun parkLongClickedManager(park: Park, v: View): Boolean {
        parkLongClicked.value = park
        return true
    }

    // CRUD
    fun removePark(park: Park) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removePark(park)
            parkList.value?.indexOf(park)?.let{
                deletedPos.postValue(it);
            }

        }
    }

    fun savePark(park: Park) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_currentPark.value != null) {
                repository.updatePark(_currentPark.value as Park)
                parkList.value
                    ?.indexOf(_currentPark.value)
                    ?.let {
                        adaptador.value?.notifyItemChanged(it)
                    }
                parkUpdated.postValue(true)
            } else {
                repository.addPark(park)
                _currentPark.postValue(park.copy())
                adaptador.value?.notifyDataSetChanged()
                parkSaved.postValue(true)
            }
        }
    }
}