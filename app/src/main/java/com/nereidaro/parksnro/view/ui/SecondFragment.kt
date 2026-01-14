package com.nereidaro.parksnro.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nereidaro.parksnro.R
import com.nereidaro.parksnro.databinding.FragmentSecondBinding
import com.nereidaro.parksnro.model.Park
import com.nereidaro.parksnro.view.dialog.MiDialogFragment

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), MiDialogFragment.OnOKOrCancelListener {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val park: Park? = arguments?.getSerializable("park") as Park?
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        populateSpinner()


        binding.btnSave.setOnClickListener {

            // 1. Recoger datos de los EditText
            val name = binding.etName.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val webPage = binding.etWebPage.text.toString().trim()

            // 2. Recoger datos de los Spinners
            val openTime = binding.spOpenTime.selectedItem?.toString() ?: ""
            val closeTime = binding.spCloseTime.selectedItem?.toString() ?: ""

            // 3. Recoger datos de las CheckBox
            val sports = binding.ckSports.isChecked
            val child = binding.ckChild.isChecked
            val bar = binding.ckBar.isChecked

            // 4. Recoger dato del RadioGroup (propiedad)
            val property = when (binding.rgProperty.checkedRadioButtonId) {
                R.id.rbPrivate -> "Private"
                R.id.rbPublic -> "Public"
                R.id.rbUnknown, View.NO_ID -> "Unknown"
                else -> "Unknown"
            }

            val msg = "Park saved: $name ($openTime - $closeTime, $property)"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showConfirmDialog() {
        val title = "Guardar parque"
        val content = "¿Quieres guardar este parque?"

        val dialog = MiDialogFragment(title, content, this)

        //Desde el fragmento, hay que pasar por requireActivity()
        dialog.show(requireActivity().supportFragmentManager, "saveDialog")
    }
    private fun populateSpinner() {
        val horas = resources.getStringArray(R.array.horas)

        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            horas
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spOpenTime.adapter = adapter
        binding.spCloseTime.adapter = adapter
    }

    override fun onPositiveClick() {
        Toast.makeText(
            requireActivity(),
            "Parque guardado",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCancelClick() {
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.actionCancelled),
            Toast.LENGTH_SHORT
        ).show()
    }
}