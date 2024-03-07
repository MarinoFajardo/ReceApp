package com.example.receapp.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.receapp.*
import com.example.receapp.Activities.RecetaActivity
import com.example.receapp.Clases.Alergenos
import com.example.receapp.Clases.Dificultad
import com.example.receapp.Clases.Receta
import com.example.receapp.Clases.Tipo
import com.example.receapp.databinding.ItemRecetaBinding

/**
 * ViewHolder utilizado para mostrar una receta en un RecyclerView.
 * @property binding Referencia a la clase de enlace generada para la actividad.
 * @property receta Receta que se va a mostrar.
 * @param view La vista del elemento de la lista.
 */
class RecetaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemRecetaBinding.bind(view)
    var receta: Receta = Receta(
        nombre = "",
        dificultad = Dificultad.Fácil,
        imagen = "",
        ingredientes = listOf(),
        pasos = listOf(),
        duracion = 0,
        tipo = Tipo.Postre,
        alergenos = listOf()
    )

    init {
        binding.cardPreview.setOnClickListener {
            mostrarReceta(receta)
        }
    }

    /**
     * Método utilizado para renderizar los datos de una receta en el ViewHolder.
     * @param recetaModel La receta a mostrar.
     */
    fun render(recetaModel: Receta) {
        // Cargar la imagen de la receta utilizando Glide
        Glide.with(binding.imagenReceta.context)
            .load(recetaModel.imagen)
            .into(binding.imagenReceta)

        // Configurar el click listener de la imagen para mostrarla en un diálogo
        binding.imagenReceta.setOnClickListener {
            val dialog = Dialog(itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_image_viewer)

            val titleTextView = dialog.findViewById<TextView>(R.id.image_title)
            titleTextView.text = recetaModel.nombre

            val imageView = dialog.findViewById<ImageView>(R.id.image_viewer)
            Glide.with(itemView.context)
                .load(recetaModel.imagen)
                .into(imageView)

            // Opcional: Configurar el diálogo para que se ajuste al tamaño de la imagen
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams

            dialog.show()
        }

        // Mostrar el nombre de la receta
        binding.nombreReceta.text = recetaModel.nombre

        // Mostrar la dificultad de la receta y configurar el ícono correspondiente
        val context: Context = itemView.context
        when (recetaModel.dificultad) {
            Dificultad.Fácil -> {
                val drawable = ContextCompat.getDrawable(context, R.drawable.baseline_whatshot_24_green)
                binding.dificultadReceta.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
            }
            Dificultad.Media -> {
                val drawable = ContextCompat.getDrawable(context, R.drawable.baseline_whatshot_24_orange)
                binding.dificultadReceta.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
            }
            else -> {
                val drawable = ContextCompat.getDrawable(context, R.drawable.baseline_whatshot_24_red)
                binding.dificultadReceta.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
            }
        }

        // Mostrar la duración de la receta
        binding.duracionReceta.text = recetaModel.duracion.toString() + " min"

        // Mostrar el tipo de la receta
        binding.tipoReceta.text = recetaModel.tipo.toString()

        // Mostrar los alergenos de la receta utilizando iconos
        val alergenos: List<Alergenos> = recetaModel.alergenos
        val iconSpans: MutableList<ImageSpan> = mutableListOf()
        for (alergeno in alergenos) {
            val iconResId: Int = when (alergeno) {
                Alergenos.Huevos -> R.drawable.egg
                Alergenos.Gluten -> R.drawable.barley
                Alergenos.Frutos -> R.drawable.peanut
                Alergenos.Marisco -> R.drawable.fish
            }
            val iconSpan = ImageSpan(itemView.context, iconResId)
            iconSpans.add(iconSpan)
        }
        val spannable = SpannableStringBuilder()
        for (icon in iconSpans) {
            spannable.append(" ")
            spannable.setSpan(icon, spannable.length - 1, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.alergenosReceta.text = spannable

        // Guardar la receta actual en el ViewHolder
        receta = recetaModel
    }

    /**
     * Método utilizado para mostrar una receta en una actividad.
     * @param recetaModel La receta a mostrar.
     */
    private fun mostrarReceta(recetaModel: Receta) {
        val intent = Intent(itemView.context, RecetaActivity::class.java)
        intent.putExtra("receta", recetaModel)
        itemView.context.startActivity(intent)
    }
}

