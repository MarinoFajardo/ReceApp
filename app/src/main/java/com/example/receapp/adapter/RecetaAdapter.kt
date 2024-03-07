package com.example.receapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.receapp.R
import com.example.receapp.Clases.Receta

/**
 * Adaptador utilizado para mostrar una lista de recetas en un RecyclerView.
 * @param recetas La lista de recetas a mostrar.
 */
class RecetaAdapter(private var recetas: List<Receta>) : RecyclerView.Adapter<RecetaViewHolder>() {

    /**
    * Función que crea una nueva instancia de [RecetaViewHolder] inflando el diseño de la vista de un elemento de receta.
    * @param parent El ViewGroup padre en el que se inflará el ViewHolder.
    * @param viewType El tipo de vista del elemento.
    * @return Una nueva instancia de [RecetaViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecetaViewHolder(layoutInflater.inflate(R.layout.item_receta, parent, false))
    }

    /**
     * Función para obtener la cantidad de recetas existentes.
     * @return la cantidad de recetas.
     */
    override fun getItemCount(): Int {
        return recetas.size
    }

    /**
     * Función para aplicar el render de visualización a una receta.
     * @param holder viewHolder de la receta.
     * @param position posición de la receta.
     */
    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val item = recetas[position]
        holder.render(item)
    }

    /**
     * Método utilizado para actualizar la lista de recetas en el adaptador.
     * @param recetas La nueva lista de recetas.
     */
    fun updateRecetas(recetas: List<Receta>) {
        this.recetas = recetas
         notifyDataSetChanged()
    }
}

