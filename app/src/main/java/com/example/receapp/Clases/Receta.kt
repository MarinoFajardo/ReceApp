package com.example.receapp.Clases

/**
 * Clase que representa una receta de cocina.
 *
 * @property nombre El nombre de la receta.
 * @property dificultad La dificultad de la receta.
 * @property imagen La URL de la imagen de la receta.
 * @property ingredientes La lista de ingredientes de la receta.
 * @property pasos La lista de pasos de la receta.
 * @property duracion La duración de la receta en minutos.
 * @property tipo El tipo de receta.
 * @property alergenos La lista de alérgenos de la receta.
 */
data class Receta (
    val nombre: String,
    val dificultad: Dificultad,
    val imagen: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val duracion: Int,
    val tipo: Tipo,
    val alergenos: List<Alergenos>
) : java.io.Serializable

