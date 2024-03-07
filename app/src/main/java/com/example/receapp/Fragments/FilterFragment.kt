package com.example.receapp.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.receapp.Clases.Alergenos
import com.example.receapp.Clases.Tipo
import com.example.receapp.DialogFragmentListener
import com.example.receapp.R

/**
 * Fragmento de diálogo utilizado para filtrar recetas.
 * @property alergenosLinearLayout Linear layout donde se encuentran los alérgenos.
 * @property listener Listener de [DialogFragment]
 * @property alergenos Lista de alérgenos seleccionados.
 * @property tipo Tipo der receta seleccionado.
 */
class FilterFragment : DialogFragment() {

    private lateinit var alergenosLinearLayout: LinearLayout
    private lateinit var listener: DialogFragmentListener
    var alergenos: MutableList<String> = arrayListOf()
    var tipo: String = ""

    /**
     * Método llamado cuando el fragmento se adjunta a una actividad.
     * Verifica que la actividad implemente la interfaz DialogFragmentListener.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogFragmentListener")
        }
    }

    /**
     * Método llamado al crear el diálogo.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Filtrar Recetas")
        val rootView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_filter, null)

        // Botones
        val cbMarisco = rootView.findViewById<CheckBox>(R.id.checkbox_marisco)
        val cbGluten = rootView.findViewById<CheckBox>(R.id.checkbox_gluten)
        val cbHuevo = rootView.findViewById<CheckBox>(R.id.checkbox_huevos)
        val cbFrutos = rootView.findViewById<CheckBox>(R.id.checkbox_frutos_secos)
        val rbEntrante = rootView.findViewById<RadioButton>(R.id.radio_entrante)
        val rbPrincipal = rootView.findViewById<RadioButton>(R.id.radio_principal)
        val rbPostre = rootView.findViewById<RadioButton>(R.id.radio_postre)

        // Tipo
        val tipoRadioGroup = rootView.findViewById<RadioGroup>(R.id.radio_group_tipo)

        // Alergenos
        alergenosLinearLayout = rootView.findViewById(R.id.alergenos_linear_layout)

        // Botón Aceptar
        val applyButton = rootView.findViewById<Button>(R.id.btn_apply)
        applyButton.setOnClickListener {
            val tipoSeleccionado = when (tipoRadioGroup.checkedRadioButtonId) {
                R.id.radio_entrante -> Tipo.Entrante
                R.id.radio_principal -> Tipo.Principal
                R.id.radio_postre -> Tipo.Postre
                else -> null
            }
            val alergenos = getSelectedAlergenos()
            listener.onPositiveButtonClicked(tipoSeleccionado, alergenos)
            dismiss()
        }

        // Botón Cancelar
        val cancelButton = rootView.findViewById<Button>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            listener.onPositiveButtonClicked(null, listOf())
            dismiss()
        }

        // Configurar los alergenos del diálogo
        alergenos = arguments?.getStringArrayList("alergenos")!!
        cbMarisco.isChecked = isAlergenoSeleccionado(Alergenos.Marisco.toString())
        cbGluten.isChecked = isAlergenoSeleccionado(Alergenos.Gluten.toString())
        cbHuevo.isChecked = isAlergenoSeleccionado(Alergenos.Huevos.toString())
        cbFrutos.isChecked = isAlergenoSeleccionado(Alergenos.Frutos.toString())

        // Configurar el tipo de receta
        tipo = arguments?.getString("tipo")!!
        when (tipo) {
            Tipo.Entrante.toString() -> rbEntrante.isChecked = true
            Tipo.Principal.toString() -> rbPrincipal.isChecked = true
            Tipo.Postre.toString() -> rbPostre.isChecked = true
            else -> {
                rbEntrante.isChecked = false
                rbPrincipal.isChecked = false
                rbPostre.isChecked = false
            }
        }

        builder.setView(rootView)
        return builder.create()
    }

    /**
     * Obtiene los alergenos seleccionados en el diálogo.
     * @return La lista de alérgenos seleccionados.
     */
    private fun getSelectedAlergenos(): List<String> {
        val selectedAlergenos: MutableList<String> = mutableListOf()

        for (i in 0 until alergenosLinearLayout.childCount) {
            val childView = alergenosLinearLayout.getChildAt(i)
            if (childView is CheckBox && childView.isChecked) {
                val alergeno = childView.text.toString()
                selectedAlergenos.add(alergeno)
            }
        }

        return selectedAlergenos
    }

    /**
     * Verifica si un alergeno está seleccionado.
     * @param aler El alérgeno a comprobar.
     * @return true si está seleccionado, false en caso contrario.
     */
    private fun isAlergenoSeleccionado(aler: String): Boolean {
        return alergenos.contains(aler)
    }

}
