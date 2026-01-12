package com.nereidaro.parksnro

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class MiDialogFragment(
    var title: String = "Titulo por defecto",
    var content: String = "Contenido por defecto",
    var frag: Fragment
) : DialogFragment() {

    interface OnOKOrCancelListener {
        fun onPositiveClick()
        fun onCancelClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder: AlertDialog.Builder =
                AlertDialog.Builder(requireActivity())

            builder.setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val listener = frag as OnOKOrCancelListener?
                    listener!!.onPositiveClick()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    val listener = frag as OnOKOrCancelListener?
                    listener!!.onCancelClick()
                }

            builder.create()

        } ?: throw IllegalStateException("El fragment no esta asociado a ninguna actividad")
    }
}
/*
Esta clase MiDialogFragment define un DialogFragment personalizado que muestra
un cuadro de dialogo con un titulo, un mensaje y dos botones: Aceptar y Cancelar.

La clase recibe por parametro un titulo, un contenido y un fragmento que actua
como listener. Dicho fragmento debe implementar la interfaz OnOKOrCancelListener
para poder responder a las acciones del dialogo.

El metodo onCreateDialog se encarga de crear el AlertDialog utilizando un Builder.
En el se establece el titulo y el mensaje del dialogo, asi como el comportamiento
de los botones.

Cuando el usuario pulsa el boton Aceptar, se llama al metodo onPositiveClick()
del fragmento listener. Cuando pulsa Cancelar, se llama al metodo onCancelClick().

De esta forma, el dialogo queda desacoplado de la logica, delegando las acciones
en el fragmento que lo invoca.
*/

