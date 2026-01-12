package com.nereidaro.parksnro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nereidaro.parksnro.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), MiDialogFragment.OnOKOrCancelListener {

    private var _binding: FragmentFirstBinding? = null
    private var itemToRemove: Park? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        //RECYCLER VIEW
        //Asociar LayoutManager, requiredActivity() es la referencia a la actividad
        binding.ParksRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        //Determinar tamaño fijo (optimización)
        binding.ParksRecyclerView.setHasFixedSize(true)
        //Asignar el adaptador, con las funciones lambda para los métodos
        binding.ParksRecyclerView.adapter = AdaptadorParques(
            {park: Park, v: View -> itemClicked(park, v)},
            {park: Park, v: View -> itemLongClicked(park, v)}
        )

        //Fin del código añadido
        return binding.root

    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        //Poblar lista de parques
        Parks.populate(requireActivity(), R.raw.parks)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //OTROS MÉTODOS
    private fun itemClicked(park: Park, v: View){
        val bundle = bundleOf("park" to park)
        v.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }
    private fun itemLongClicked(park: Park, v: View): Boolean{
        itemToRemove = park

        //Invocar al diálogo de confirmación
        val miDialogo = MiDialogFragment(
            resources.getString(R.string.deletePark),
            resources.getString(R.string.askDeletePark)+park.name+"?",
            this
        )

        //Para acceder al supportFragmentManager hay que pasar por la actividad
        miDialogo.show(requireActivity().supportFragmentManager, "miDialogo")
        return true
    }
    override fun onPositiveClick() {
        itemToRemove?.also{
            val index = Parks.remove(it)
            binding.ParksRecyclerView.adapter?.notifyItemRemoved(index)
        }
    }

    override fun onCancelClick() {
        Toast.makeText(
            requireActivity().applicationContext,
            resources.getString(R.string.actionCancelled),
            Toast.LENGTH_SHORT
        ).show()
    }
}