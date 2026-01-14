package com.nereidaro.parksnro.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nereidaro.parksnro.R
import com.nereidaro.parksnro.databinding.FragmentFirstBinding
import com.nereidaro.parksnro.model.Park
import com.nereidaro.parksnro.view.dialog.MiDialogFragment
import com.nereidaro.parksnro.viewmodel.FirstFragmentViewModel
//Vista en firstFragment y lógica en FirstFragmentViewModel -> MVVM
class FirstFragment : Fragment(), MiDialogFragment.OnOKOrCancelListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var firstFragmentViewModel: FirstFragmentViewModel

    // Guardamos el parque a eliminar cuando llegue el long click
    private var itemToRemove: Park? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // 1) Instanciamos el ViewModel
        firstFragmentViewModel =
            ViewModelProvider(this)[FirstFragmentViewModel::class.java]

        // 2) Configuramos RecyclerView (responsabilidad de la vista)
        binding.ParksRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.ParksRecyclerView.setHasFixedSize(true)

        // 3) Observamos el adaptador que nos expone el ViewModel
        firstFragmentViewModel.adaptador.observe(viewLifecycleOwner) { adapter ->
            binding.ParksRecyclerView.adapter = adapter
        }

        // 4) Observamos click normal: navegar al SecondFragment
        firstFragmentViewModel.parkClicked.observe(viewLifecycleOwner) { park ->
            park?.let {
                // IMPORTANTE: reseteamos a null para no repetir evento en rotaciones
                firstFragmentViewModel.parkClicked.value = null

                val bundle = bundleOf("park" to it)
                binding.root.findNavController()
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
            }
        }

        // 5) Observamos long click: abrir diálogo de confirmación
        firstFragmentViewModel.parkLongClicked.observe(viewLifecycleOwner) { park ->
            park?.let {
                firstFragmentViewModel.parkLongClicked.value = null
                itemToRemove = it

                val miDialogo = MiDialogFragment(
                    resources.getString(R.string.deletePark),
                    resources.getString(R.string.askDeletePark) + it.name + "?",
                    this
                )

                miDialogo.show(requireActivity().supportFragmentManager, "miDialogo")
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Diálogo: OK => borrar (pero borrado lo hace el ViewModel)
    override fun onPositiveClick() {
        itemToRemove?.let { park ->
            firstFragmentViewModel.removePark(park)
            itemToRemove = null
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