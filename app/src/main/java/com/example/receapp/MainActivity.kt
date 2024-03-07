package com.example.receapp

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receapp.Clases.Receta
import com.example.receapp.Clases.Tipo
import com.example.receapp.DataBase.RecetaProvider
import com.example.receapp.Fragments.FilterFragment
import com.example.receapp.adapter.RecetaAdapter
import com.example.receapp.databinding.ActivityMainBinding


/**
 * Interfaz que define el listener para el diálogo de fragmento.
 */
interface DialogFragmentListener {
    /**
     * Función llamada cuando se hace clic en el botón positivo del diálogo.
     * @param value El valor del tipo seleccionado. Puede ser nulo.
     * @param alergenos La lista de alérgenos seleccionados.
     */
    fun onPositiveButtonClicked(value: Tipo?, alergenos: List<String>)
}

/**
 * Actividad principal de la aplicación. En ella se muestra la lista de recetas disponibles.
 * @property TABLE_NAME Nombre de la tabla usada en la base de datos.
 * @property dbHelper Instancia de la clase que contiene la base de datos.
 * @property db Instancia de SQLiteDatabase para acceder a la base de datos.
 * @property binding Instancia del binding para el diseño.
 * @property adapter Instancia del adapter para mostrar las recetas.
 * @property llmanager El Layout Manager de diseño de lista de recetas.
 * @property alergenosList Lista de alérgenos seleccionados en el filtro.
 * @property tipoFiltro Tipo de receta seleccionado en el filro.
 * @property listaRecetasBDD Lista de recetas que se encuentran en RecetasProvider.
 * @property listaRecetas Lista de recetas que se encuentran en la base de datos.
 */
class MainActivity : AppCompatActivity(), DialogFragmentListener {

    private val TABLE_NAME = "Receta"
    private lateinit var dbHelper: DBHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecetaAdapter
    private var llmanager = LinearLayoutManager(this)
    private var alergenosList: List<String> = listOf()
    private var tipoFiltro: String = ""
    private var listaRecetasBDD: List<Receta> = RecetaProvider.recetas
    private var listaRecetas: List<Receta> = listOf()

    /**
     * Función llamada al crear la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(this)

        /**
         * Se comprueba si existe la base de datos:
         *      - Si existe se comprueba si hay uevas recetas en Recetas Provider y se añaden
         *      - Si no existe se crea.
         */
        if (!checkDatabaseExists()) {
            dbHelper.createDatabase()
            insetarRecetasBDD(listaRecetasBDD)
        } else {
            addNewRecipesToDatabase()
        }

        /**
         * Leemos las recetas que hay en la base de datos.
         */
        db = dbHelper.readableDatabase
        listaRecetas = recetasBDD(db)
        db.close()

        /**
         * Asignamos valores al adapter y al recyclerview donde se muestran las recetas.
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        adapter = RecetaAdapter(
            recetas = listaRecetas
        )
        binding.recyclerViewRecetas.layoutManager = llmanager
        binding.recyclerViewRecetas.adapter = adapter
        val decoration = DividerItemDecoration(this, llmanager.orientation)
        binding.recyclerViewRecetas.addItemDecoration(decoration)
        setContentView(binding.root)

        /**
         * Funcionamiento del filtro que busca por el nombre de la receta.
         */
        binding.filtro.addTextChangedListener { recetaFilter ->
            val recetasFiltered = listaRecetas.filter { receta ->
                receta.nombre.lowercase().contains(recetaFilter.toString().lowercase())
            }
            adapter.updateRecetas(recetasFiltered)
        }
    }

    /**
     * Función para crear el menú de opciones en la barra de acción.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filtro, menu)
        return true
    }

    /**
     * Función para manejar la selección de una opción del menú de acción.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Función para mostrar el diálogo de filtro.
     */
    private fun showFilterDialog() {
        val filter = FilterFragment()
        val args = Bundle().apply {
            putStringArrayList("alergenos", ArrayList(alergenosList))
            putString("tipo", tipoFiltro)
        }
        filter.arguments = args
        filter.show(supportFragmentManager, "FilterDialog")
    }

    /**
     * Función llamada cuando se hace clic en el botón positivo del diálogo de filtro.
     * @param value El valor del tipo seleccionado. Puede ser nulo.
     * @param alergenos La lista de alérgenos seleccionados.
     */
    override fun onPositiveButtonClicked(value: Tipo?, alergenos: List<String>) {
        /**
         * Vamos comprobando si la lista de alérgenos y el tipo de las recetas procedentes del filtro están vacías o no
         * y filtramos las recetas en función del resultado.
         */
        alergenosList = alergenos
        Log.d("Alergenos", alergenosList.toString())
        if (value != null) {
            tipoFiltro = value.toString()
        }
        if (value != null && alergenos.isEmpty()) {
            val filtradas = listaRecetas.filter { it.tipo == value }
            adapter.updateRecetas(filtradas)
        } else if (value != null && alergenos.isNotEmpty()) {
            val noAlergenos = listaRecetas.filter { receta ->
                alergenos.all { alergeno ->
                    receta.alergenos.none { al ->
                        al.toString().compareTo(alergeno) == 0
                    }
                }
            }
            val filtradas = noAlergenos.filter { it.tipo == value }
            adapter.updateRecetas(filtradas)
        } else if (value == null && alergenos.isNotEmpty()) {
            val noAlergenos = listaRecetas.filter { receta ->
                alergenos.all { alergeno ->
                    receta.alergenos.none { al ->
                        al.toString().compareTo(alergeno) == 0
                    }
                }
            }
            adapter.updateRecetas(noAlergenos)
        } else {
            tipoFiltro = ""
            adapter.updateRecetas(listaRecetas)
        }
    }

    /**
     * Función para insertar las recetas en la base de datos.
     * @param recetas La lista de recetas a insertar.
     */
    private fun insetarRecetasBDD(recetas: List<Receta>) {
        db = dbHelper.writableDatabase
        for (receta in recetas) {
            dbHelper.addReceta(receta, db)
        }
        db.close()
    }

    /**
     * Función para obtener las recetas de la base de datos.
     * @param db La instancia de la base de datos SQLite.
     * @return La lista de recetas obtenidas de la base de datos.
     */
    private fun recetasBDD(db: SQLiteDatabase): List<Receta> {
        val recetas = mutableListOf<Receta>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val receta = dbHelper.getReceta(cursor)
                recetas.add(receta)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return recetas
    }

    /**
     * Función para verificar si la base de datos existe.
     * @return true si la base de datos existe, false de lo contrario.
     */
    private fun checkDatabaseExists(): Boolean {
        return try {
            val db = dbHelper.readableDatabase
            val path = db.path
            db.close()
            path != null
        } catch (e: SQLiteException) {
            false
        }
    }

    /**
     * Función para agregar nuevas recetas a la base de datos.
     */
    private fun addNewRecipesToDatabase() {
        db = dbHelper.writableDatabase

        val existingRecipeIds = dbHelper.getExistingRecipeIds(db)
        val newRecipes = listaRecetasBDD.filter { receta ->
            !existingRecipeIds.contains(receta.nombre)
        }

        for (receta in newRecipes) {
            dbHelper.addReceta(receta, db)
        }

        db.close()
    }
}