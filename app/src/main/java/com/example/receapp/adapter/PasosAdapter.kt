package com.example.receapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.receapp.R

/**
 * Adaptador utilizado para mostrar una lista de pasos en un ViewPager.
 *
 * @property pasos La lista de pasos a mostrar.
 */
class PasosAdapter(private val pasos: List<String>) : PagerAdapter() {

    /**
     * Función que devuelve la cantidad de pasos de una receta.
     *
     * @return la cantidad de pasos.
     */
    override fun getCount(): Int {
        return pasos.size
    }

    /**
     * Verifica si la vista del objeto es igual a la vista proporcionada.
     *
     * @param view La vista proporcionada.
     * @param object El objeto proporcionado.
     * @return true si la vista del objeto es igual a la vista proporcionada, de lo contrario, false.
     */
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    /**
     * Crea y devuelve la vista de un elemento en la posición dada.
     *
     * @param container El contenedor donde se colocará la vista.
     * @param position La posición del elemento en la lista.
     * @return La vista creada.
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val itemView = inflater.inflate(R.layout.item_paso, container, false)

        val tvPaso = itemView.findViewById<TextView>(R.id.tvPasos)
        tvPaso.text = pasos[position]

        container.addView(itemView)
        return itemView
    }

    /**
     * Elimina una vista del contenedor en la posición dada.
     *
     * @param container El contenedor donde se encuentra la vista.
     * @param position La posición del elemento en la lista.
     * @param object El objeto que representa la vista a eliminar.
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

