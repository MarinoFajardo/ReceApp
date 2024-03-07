package com.example.receapp.Activities

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.example.receapp.Clases.Dificultad
import com.example.receapp.Clases.Receta
import com.example.receapp.Clases.Tipo
import com.example.receapp.R
import com.example.receapp.adapter.PasosAdapter
import com.example.receapp.databinding.ActivityPreparacionBinding
import java.util.*
import kotlin.math.abs

/**
 * Activity encargada de la preparación de una receta.
 *
 * Esta actividad muestra los pasos de una receta en forma de presentación de diapositivas y permite
 * controlar la reproducción de los pasos mediante botones y comandos de voz.
 *
 * @property binding Referencia a la clase de enlace generada para la actividad.
 * @property textToSpech Instancia de TextToSpeech utilizada para reproducir el texto de los pasos en voz alta.
 * @property speechRecognizer Instancia de SpeechRecognizer utilizada para reconocer comandos de voz.
 * @property viewPager Referencia al ViewPager utilizado para mostrar los pasos de la receta.
 * @property pasosAdapter Adaptador utilizado para mostrar los pasos en el ViewPager.
 * @property btnAnterior Botón para retroceder al paso anterior.
 * @property btnSiguiente Botón para avanzar al siguiente paso.
 * @property btnAsistente Botón para iniciar el reconocimiento de voz.
 * @property btnStop Botón para detener la reproducción de sonido del temporizador.
 * @property timerTextView TextView utilizado para mostrar el temporizador.
 * @property mediaPlayer Instancia de MediaPlayer utilizada para reproducir el sonido del temporizador.
 * @property linearTemp LinearLayout utilizado para mostrar el temporizador.
 * @property audioManager Instancia de AudioManager utilizada para controlar el volumen del sonido.
 * @property currentVolume Volumen actual del dispositivo.
 * @property timer Instancia de CountDownTimer utilizada para llevar el tiempo del temporizador.
 * @property pasos Lista de pasos de la receta.
 * @property posActual Posición actual del paso en el ViewPager.
 * @property isTimerRunning Indica si el temporizador está en ejecución.
 * @property receta Receta asociada a la actividad.
 */
class PreparacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreparacionBinding
    private lateinit var textToSpech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var viewPager: ViewPager
    private lateinit var pasosAdapter: PasosAdapter
    private lateinit var btnAnterior: Button
    private lateinit var btnSiguiente: Button
    private lateinit var btnAsistente: Button
    lateinit var btnStop: Button
    lateinit var timerTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var linearTemp: LinearLayout
    private lateinit var audioManager: AudioManager
    private var currentVolume: Int = 0
    private var timer: CountDownTimer? = null
    private var pasos: List<String> = listOf()
    private var posActual: Int = 0
    private var isTimerRunning: Boolean = false
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

    /**
     * Método llamado cuando se crea la actividad.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreparacionBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_preparacion)
        receta = intent.getSerializableExtra("receta") as Receta
        pasos = receta.pasos

        //Inicializar el textToSpeech y el spechtoText
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        textToSpech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                reproducirTexto()
            }
        }

        // nombre
        supportActionBar?.title = receta.nombre
        // Volver atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Configurar el viewPager
        viewPager = findViewById(R.id.viewPager)
        pasosAdapter = PasosAdapter(pasos)
        viewPager.adapter = pasosAdapter
        viewPager.setOnTouchListener { _, _ -> true }
        viewPager.setPageTransformer(true, DepthPageTransformer())

        //Timer
        timerTextView = findViewById(R.id.temporizador)

        //Sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mediaPlayer.isLooping = true

        //Botones
        btnAnterior = findViewById(R.id.btnAnterior)
        btnSiguiente = findViewById(R.id.btnSiguiente)
        btnAsistente = findViewById(R.id.boton_asistente)
        btnStop = findViewById(R.id.btnStop)
        btnAnterior.setOnClickListener { retrocederPaso() }
        btnSiguiente.setOnClickListener { avanzarPaso() }
        btnAsistente.setOnClickListener { iniciarReconocimientoVoz() }
        btnStop.isVisible = false
        btnStop.setOnClickListener {
            stopMediaPlayer()
        }

        //Hacer que el temporizador se muestre solo cuando hay un timer activo
        linearTemp = findViewById(R.id.linear_temp)
        linearTemp.visibility = View.GONE

        //inicializar volumen
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    /**
     * Método para avanzar al siguiente paso.
     */
    private fun avanzarPaso() {
        if(timer != null){
            timer?.cancel()
            linearTemp.isVisible = false
        }
        if (posActual < pasos.size - 1) {
            posActual++
            viewPager.currentItem = posActual
            actualizarBotones()
            reproducirTexto()
        } else if (posActual == pasos.size - 1) {
            if(textToSpech.isSpeaking){
                textToSpech.stop()
            }
            finish()
        }
    }

    /**
     * Método para retroceder al paso anterior.
     */
    private fun retrocederPaso() {
        if(timer != null){
            timer?.cancel()
            linearTemp.isVisible = false
        }
        if (posActual > 0) {
            posActual--
            viewPager.currentItem = posActual
            actualizarBotones()
            reproducirTexto()
        }else{
            if(textToSpech.isSpeaking){
                textToSpech.stop()
            }
            finish()
        }
    }

    /**
     * Método para actualizar el texto de los botones de avanzar y retroceder.
     */
    private fun actualizarBotones() {
        when (posActual) {
            0 -> {
                btnAnterior.text = "Close"
            }
            pasos.size - 1 -> {
                btnSiguiente.text = "End"
            }
            else -> {
                btnSiguiente.text = "Next"
                btnAnterior.text = "Prev"
            }
        }
    }

    /**
     * Método para reproducir el texto del paso actual.
     */
    private fun reproducirTexto() {
        val texto = pasos[posActual]
        val maxVolumen = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val umbral = maxVolumen / 15
        if(currentVolume < umbral){
            Toast.makeText(this, "Por favor, aumenta el volumen para escuchar el paso", Toast.LENGTH_LONG).show()
        }
        textToSpech.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    /**
     * Método para iniciar el reconocimiento de voz.
     */
    private fun iniciarReconocimientoVoz() {
        if(textToSpech.isSpeaking){
            textToSpech.stop()
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        startActivityForResult(intent, 1)
    }

    /**
     * Método para obtener el comando creado por el reconocimiento de voz.
     * @param requestCode código de la petición.
     * @param resultCode código resultado de la actividad.
     * @param data valor obtenido.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val resultados = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!resultados.isNullOrEmpty()) {
                val comando = resultados[0].lowercase(Locale.getDefault())
                if (comando.contains("repite")||comando.contains("reproduce")||comando.contains("lee")||comando.contains("dicta")) {
                    reproducirTexto()
                }else if(comando.contains("siguiente")){
                    avanzarPaso()
                }else if(comando.contains("anterior")){
                    retrocederPaso()
                }else if(comando.contains("finaliza")||comando.contains("termina")){
                    finish()
                }else if(comando.contains("minutos")){
                    Log.d("orden: ",comando)
                    val duration = extractDuration(comando)
                    Log.d("tiempo: ",duration.toString())
                    if(duration > 0){
                        startTimer(duration)
                    }
                }
            }
        }
    }

    /**
     * Función que extrae la duración del temporizador
     * @param command comando del que extraer la duración del timer.
     */
    private fun extractDuration(command:String): Long {
        val regex = Regex("(\\w+)\\s+(minutos|minuto)")
        val matchResult = regex.find(command)
        if (matchResult != null) {
            val durationString = matchResult.groupValues[1]
            val duration = convertirPalabraAMinutos(durationString)
            if(duration != null){
                return duration
            }
        }

        return 0
    }

    /**
     * Función para iniciar el emporizador.
     * @param minutes duración en minutos.
     */
    private fun startTimer(minutes: Long) {
        if (timer != null) {
            timer?.cancel()
        }
        linearTemp.visibility = View.VISIBLE
        isTimerRunning = true
        val milliseconds = minutes * 60 * 1000
        timer = object : CountDownTimer(milliseconds, 1000) {
            /**
             * Función que se ejecuta en cada tick del temporizador.
             * @param millisUntilFinished tiempo hasta que finalice el temporizador.
             */
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutesRemaining = secondsRemaining / 60
                val secondsDisplay = secondsRemaining % 60
                timerTextView.text = String.format(Locale.getDefault(), "%02d:%02d", minutesRemaining, secondsDisplay)
            }

            /**
             * Función que se ejecuta al finalizar el temporizador.
             */
            override fun onFinish() {
                playSound()
                if(isAppInBackground()){
                    timerTextView.text = "00:00"
                    btnStop.isVisible = true
                    mostrarNotificacion()
                }else{
                    timerTextView.text = "00:00"
                    btnStop.isVisible = true
                }
            }
        }.start()
    }

    /**
     * Función para reproducir el sonido de la alarma.
     */
    fun playSound(){
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        //volumen Maximo
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
        mediaPlayer.start()

    }

    /**
     * Función para finalizar el sonido de la alarma.
     */
    private fun stopMediaPlayer(){
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare()
            linearTemp.visibility = View.GONE
            btnStop.isVisible = false
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
            mediaPlayer.release()

        }
    }

    /**
     * Función que se ejecuta al finalizar la actividad.
     */
    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        mediaPlayer.release()
    }

    /**
     * Función para convertir una palabra en minutos
     * @param palabra la palabra que queremos convertir.
     * @return la conversión en minutos.
     */
    private fun convertirPalabraAMinutos(palabra: String): Long? {
        val palabrasNumericas = mapOf(
            "uno" to 1L,
            "dos" to 2L,
            "tres" to 3L,
            "cuatro" to 4L,
            "cinco" to 5L,
            "seis" to 6L,
            "siete" to 7L,
            "ocho" to 8L,
            "nueve" to 9L,
            "diez" to 10L,
            "once" to 11L,
            "doce" to 12L,
            "trece" to 13L,
            "catorce" to 14L,
            "quince" to 15L,
            "dieciséis" to 16L,
            "diecisiete" to 17L,
            "dieciocho" to 18L,
            "diecinueve" to 19L,
            "veinte" to 20L,
            "veintiuno" to 21L,
            "veintidós" to 22L,
            "veintitrés" to 23L,
            "veinticuatro" to 24L,
            "veinticinco" to 25L,
            "veintiséis" to 26L,
            "veintisiete" to 27L,
            "veintiocho" to 28L,
            "veintinueve" to 29L,
            "treinta" to 30L,
            "treinta y uno" to 31L,
            "treinta y dos" to 32L,
            "treinta y tres" to 33L,
            "treinta y cuatro" to 34L,
            "treinta y cinco" to 35L,
            "treinta y seis" to 36L,
            "treinta y siete" to 37L,
            "treinta y ocho" to 38L,
            "treinta y nueve" to 39L,
            "cuarenta" to 40L,
            "cuarenta y uno" to 41L,
            "cuarenta y dos" to 42L,
            "cuarenta y tres" to 43L,
            "cuarenta y cuatro" to 44L,
            "cuarenta y cinco" to 45L,
            "cuarenta y seis" to 46L,
            "cuarenta y siete" to 47L,
            "cuarenta y ocho" to 48L,
            "cuarenta y nueve" to 49L,
            "cincuenta" to 50L,
            "cincuenta y uno" to 51L,
            "cincuenta y dos" to 52L,
            "cincuenta y tres" to 53L,
            "cincuenta y cuatro" to 54L,
            "cincuenta y cinco" to 55L,
            "cincuenta y seis" to 56L,
            "cincuenta y siete" to 57L,
            "cincuenta y ocho" to 58L,
            "cincuenta y nueve" to 59L,
            "sesenta" to 60L
        )

        return palabrasNumericas[palabra.lowercase(Locale.ROOT)]
    }

    /**
     * Función que comprueba si la aplicación está en segundo plano.
     * @return true si está, false en caso contrario.
     */
    fun isAppInBackground():Boolean{
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses

        for (appProcess in appProcesses) {
            if (appProcess.processName == packageName) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return false
                }
            }
        }

        return true
    }

    /**
     * Función para mostrar una notificación.
     */
    fun mostrarNotificacion() {
        val notificationId = 1 // Identificador único de la notificación

        val channelId = "timer_channel" // Identificador del canal de notificación
        val channelName = "Timer Channel" // Nombre del canal de notificación
        val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val descriptionText = "Notificación para apagar temporizador"
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }
            //Registrar el Canal
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Timer finalizado")
            .setContentText("Haz clic para detener el sonido del temporizador")
            .setSmallIcon(R.drawable.bell_ring)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_PUBLIC)

        // Detener el sonido del temporizador utilizando PendingIntent

        val notificationIntent = Intent(this, PreparacionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setAutoCancel(true)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    /**
     * Función que se ejecuta al pulsar el botón de volver atrás.
     */
    override fun onBackPressed() {
        if(textToSpech.isSpeaking){
            textToSpech.stop()
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Acción de volver atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

private const val MIN_SCALE = 0.75f

/**
 * Transformador de páginas para ViewPager que aplica un efecto de profundidad a las transiciones entre páginas.
 */
class DepthPageTransformer : ViewPager.PageTransformer {

    /**
     * Método para transformar una página.
     *
     * @param view La vista de la página.
     * @param position La posición relativa de la página.
     */
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // Esta página está completamente fuera de la pantalla a la izquierda.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Utilizar la transición de deslizamiento predeterminada al moverse a la página izquierda.
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // (0,1]
                    // Desvanecer la página.
                    alpha = 1 - position

                    // Contrarrestar la transición de deslizamiento predeterminada
                    translationX = pageWidth * -position

                    // Escalar la página hacia abajo (entre MIN_SCALE y 1)
                    val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // Esta página está completamente fuera de la pantalla a la derecha.
                    alpha = 0f
                }
            }
        }
    }
}
