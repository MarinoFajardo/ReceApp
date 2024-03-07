package com.example.receapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.receapp.Clases.Alergenos
import com.example.receapp.Clases.Dificultad
import com.example.receapp.Clases.Receta
import com.example.receapp.Clases.Tipo


/**
 * Clase de ayuda para la administración de la base de datos SQLite utilizada para almacenar recetas.
 *
 * @property context El contexto de la aplicación.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Onjeto que contiene los valores de los campos de la base de datos.
     */
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Recetas.db"
        private const val TABLE_NAME = "Receta"
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_DURACION = "duracion"
        private const val KEY_IMAGEN = "imagen"
        private const val KEY_INGREDIENTES = "ingredientes"
        private const val KEY_PASOS = "pasos"
        private const val KEY_DIFICULTAD = "dificultad"
        private const val KEY_TIPO = "tipo"
        private const val KEY_ALERGENOS = "alergenos"
    }

    /**
     * Se llama cuando la base de datos se crea por primera vez.
     *
     * @param db La base de datos recién creada.
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_NOMBRE TEXT NOT NULL,"
                + "$KEY_DURACION INTEGER NOT NULL,"
                + "$KEY_IMAGEN TEXT NOT NULL,"
                + "$KEY_INGREDIENTES TEXT NOT NULL,"
                + "$KEY_PASOS TEXT NOT NULL,"
                + "$KEY_DIFICULTAD TEXT NOT NULL,"
                + "$KEY_TIPO TEXT NOT NULL,"
                + "$KEY_ALERGENOS TEXT NOT NULL)")
        db.execSQL(createTableQuery)
    }

    /**
     * Se llama cuando se necesita actualizar la base de datos.
     *
     * @param db La base de datos existente.
     * @param oldVersion La versión antigua de la base de datos.
     * @param newVersion La nueva versión de la base de datos.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Agrega una receta a la base de datos.
     *
     * @param receta La receta a agregar.
     * @param db La instancia de la base de datos.
     */
    fun addReceta(receta: Receta, db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(KEY_NOMBRE, receta.nombre)
            put(KEY_DURACION, receta.duracion)
            put(KEY_IMAGEN, receta.imagen)
            put(KEY_INGREDIENTES, receta.ingredientes.joinToString(","))
            put(KEY_PASOS, receta.pasos.joinToString("\n"))
            put(KEY_DIFICULTAD, receta.dificultad.toString())
            put(KEY_TIPO, receta.tipo.toString())
            put(KEY_ALERGENOS, receta.alergenos.joinToString(","))
        }
        db.insert(TABLE_NAME, null, values)
    }

    /**
     * Obtiene una receta a partir del cursor actual.
     *
     * @param cursor El cursor actual.
     * @return La receta obtenida.
     */
    fun getReceta(cursor: Cursor): Receta {
        val nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE))
        val dificultadStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DIFICULTAD))
        val imagen = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGEN))
        val ingredientesStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_INGREDIENTES))
        val pasosStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASOS))
        val duracion = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DURACION))
        val tipoStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIPO))
        val alergenosStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ALERGENOS))

        val dificultad = Dificultad.valueOf(dificultadStr)
        val ingredientes = ingredientesStr.split(",")
        val pasos = pasosStr.split("\n")
        val tipo = Tipo.valueOf(tipoStr)
        val alergenos = alergenosStr.split(",").map { Alergenos.valueOf(it) }

        return Receta(
            nombre = nombre,
            dificultad = dificultad,
            imagen = imagen,
            ingredientes = ingredientes,
            pasos = pasos,
            duracion = duracion,
            tipo = tipo,
            alergenos = alergenos
        )
    }

    /**
     * Crea la base de datos si no existe.
     */
    fun createDatabase() {
        val db = writableDatabase
        db.execSQL("CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_NOMBRE TEXT NOT NULL, $KEY_DURACION INTEGER NOT NULL, $KEY_IMAGEN TEXT NOT NULL, $KEY_INGREDIENTES TEXT NOT NULL, $KEY_PASOS TEXT NOT NULL, $KEY_DIFICULTAD TEXT NOT NULL, $KEY_TIPO TEXT NOT NULL, $KEY_ALERGENOS TEXT NOT NULL)")
        db.close()
    }

    /**
     * Obtiene los nombres de recetas existentes en la base de datos.
     *
     * @param db La instancia de la base de datos.
     * @return Lista de nombres de recetas existentes.
     */
    fun getExistingRecipeIds(db: SQLiteDatabase): List<String> {
        val existingIds = mutableListOf<String>()
        val query = "SELECT $KEY_NOMBRE FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE))
                existingIds.add(nombre)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return existingIds
    }

}
