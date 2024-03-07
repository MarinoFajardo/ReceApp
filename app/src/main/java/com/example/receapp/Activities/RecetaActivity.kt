package com.example.receapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.receapp.Clases.Alergenos
import com.example.receapp.Clases.Dificultad
import com.example.receapp.Clases.Receta
import com.example.receapp.Clases.Tipo
import com.example.receapp.Activities.PreparacionActivity
import com.example.receapp.R
import com.example.receapp.databinding.ActivityRecetaBinding

/**
 * Actividad para mostrar los detalles de una receta.
 *
 * @property binding Referencia del binding usado para el diseño.
 * @property receta Referencia a la receta que está siendo mostrada.
 */
class RecetaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecetaBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecetaBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_receta)
        receta = intent.getSerializableExtra("receta") as Receta

        // Configurar la barra de acción con el nombre de la receta
        supportActionBar?.title = receta.nombre

        // Configurar la duración de la receta
        val duracion = findViewById<TextView>(R.id.duracion_ver_receta)
        duracion.text = receta.duracion.toString() + " min"

        // Configurar la dificultad de la receta
        val dificultad = findViewById<TextView>(R.id.dificultad_ver_receta)
        dificultad.text = receta.dificultad.toString()
        when (receta.dificultad) {
            Dificultad.Fácil -> {
                val drawable = ContextCompat.getDrawable(
                    this,
                    R.drawable.baseline_whatshot_24_green
                )
                dificultad.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null
                )
            }
            Dificultad.Media -> {
                val drawable = ContextCompat.getDrawable(
                    this,
                    R.drawable.baseline_whatshot_24_orange
                )
                dificultad.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null
                )
            }
            else -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.baseline_whatshot_24_red)
                dificultad.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null
                )
            }
        }

        // Configurar el tipo de receta
        val tipo = findViewById<TextView>(R.id.tipo_ver_receta)
        tipo.text = receta.tipo.toString()

        // Configurar la imagen de la receta
        val imagen = findViewById<ImageView>(R.id.imagen_ver_receta)
        Glide.with(imagen).load(receta.imagen).into(imagen)

        // Configurar la lista de ingredientes
        val adapterIngredientes = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            receta.ingredientes
        )
        val ingredientes = findViewById<ListView>(R.id.lista_ingredientes)
        ingredientes.adapter = adapterIngredientes

        // Configurar los alérgenos de la receta
        val alergenos: List<Alergenos> = receta.alergenos
        val iconSpans: MutableList<ImageSpan> = mutableListOf()
        for (alergeno in alergenos) {
            val iconResId: Int = when (alergeno) {
                Alergenos.Huevos -> R.drawable.egg
                Alergenos.Gluten -> R.drawable.barley
                Alergenos.Frutos -> R.drawable.peanut
                Alergenos.Marisco -> R.drawable.fish
            }
            val iconSpan = ImageSpan(this, iconResId)
            iconSpans.add(iconSpan)
        }
        val spannable = SpannableStringBuilder()
        for (icon in iconSpans) {
            spannable.append(" ")
            spannable.setSpan(
                icon,
                spannable.length - 1,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val textAlergenos = findViewById<TextView>(R.id.alergenos_ver_receta)
        textAlergenos.text = spannable

        // Configurar el botón de preparación
        val preparacion = findViewById<Button>(R.id.boton_comenzar_receta)
        preparacion.setOnClickListener {
            preparacionReceta(receta)
        }
    }

    /**
     * Inicia la actividad de preparación de la receta.
     * @param receta La receta a preparar.
     */
    private fun preparacionReceta(receta: Receta) {
        val intent = Intent(this, PreparacionActivity::class.java)
        intent.putExtra("receta", receta)
        startActivityForResult(intent, 1)
    }
}
