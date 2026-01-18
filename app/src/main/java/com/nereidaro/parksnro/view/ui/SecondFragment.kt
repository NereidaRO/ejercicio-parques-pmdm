package com.nereidaro.parksnro.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nereidaro.parksnro.R
import com.nereidaro.parksnro.databinding.FragmentSecondBinding
import com.nereidaro.parksnro.db.Park
import com.nereidaro.parksnro.view.dialog.MiDialogFragment
import com.nereidaro.parksnro.viewmodel.FirstFragmentViewModel
import android.Manifest
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), MiDialogFragment.OnOKOrCancelListener {
    private lateinit var firstFragmentViewModel: FirstFragmentViewModel
    private var _binding: FragmentSecondBinding? = null

    //Permisos de la cámara
    private val cameraPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            if (
                permissions.getOrDefault(
                    android.Manifest.permission.CAMERA,
                    false
                ) &&
                permissions.getOrDefault(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    false
                )
            ) {
                // Si tenemos permisos, invocamos el método takePhoto()
                takePhoto()
            } else {
                // Si no, mostramos una advertencia
                Log.w("Permisos de cámara", "Sin permisos")
            }
        }

    //Guardar URI de la foto en URI y en texto plano
    private var latestTmpUri: Uri? = null
    private var tmpUri = ""
    private val cargadorCamara =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { result: Boolean ->

            if (result) {
                binding.iwHero.setImageURI(latestTmpUri)
                tmpUri = latestTmpUri.toString()
                firstFragmentViewModel.currentPark.value?.tmpUri = tmpUri
            }
        }
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
        firstFragmentViewModel = ViewModelProvider(requireActivity())[FirstFragmentViewModel::class.java]
        firstFragmentViewModel.parkSaved.observe(viewLifecycleOwner) { saved ->
            saved?.let {
                firstFragmentViewModel.parkSaved.value = null
                // ImageView
                if (park?.tmpUri != "")
                    binding.iwHero.setImageURI(Uri.parse(park?.tmpUri))
                else
                    binding.iwHero.setImageResource(R.drawable.pexels_hero)
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.dataSaved),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        firstFragmentViewModel.parkUpdated.observe(viewLifecycleOwner) { updated ->
            updated?.let {
                firstFragmentViewModel.parkUpdated.value = null
                // ImageView
                if (park?.tmpUri != "")
                    binding.iwHero.setImageURI(Uri.parse(park?.tmpUri))
                else
                    binding.iwHero.setImageResource(R.drawable.pexels_hero)
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.updatedSaved),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.iwHero.setOnClickListener {

            // Cuando se hace clic sobre la imagen, lanzamos el
            // cargador para comprobar los permisos de la cámara
            cameraPermissionRequest.launch(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
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
    private fun takePhoto(){
        // Obtenemos la carpeta de almacenamiento externo de la aplicación, mediante el método getExternalFilesDir de la actividad
        val dir = requireActivity()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Generamos el fichero temporal
        val file = File.createTempFile("parques", ".jpg", dir)

        // Obtenemos su URI, almacenándola en la variable latestTmpUri, definida como variable miembro de la clase
        latestTmpUri =
            FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().applicationContext.packageName +
                        ".provider",
                file
            )

        // Y proporcionamos esta ruta al cargador de la cámara, para que guarde la imagen capturada en la URI especificada.
        cargadorCamara.launch(latestTmpUri)
    }
}