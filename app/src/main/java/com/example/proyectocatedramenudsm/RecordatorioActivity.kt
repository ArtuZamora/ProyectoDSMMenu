package com.example.proyectocatedramenudsm

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.proyectocatedramenudsm.servicios.RecordatorioReceiver
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar


class RecordatorioActivity : AppCompatActivity() {
    private lateinit var cerrarBtn: ImageView
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var mostrarHora: EditText
    private lateinit var calendar: Calendar
    private lateinit var establecerBtn: Button
    private lateinit var cancelarBtn: Button
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var tiempoActual: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordatorio)
        sharedPreferences = applicationContext.getSharedPreferences("DSM_CONFIGURACIONES", MODE_PRIVATE)
        inicializarUI()
        createNotificationChannel()
        cargarConfiguraion()

        cerrarBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        establecerBtn.setOnClickListener{
            setAlarm()
        }
        cancelarBtn.setOnClickListener{
            cancelAlarm()
            mostrarHora.setText(null)
        }
        mostrarHora.setOnClickListener{
            mostrarTimePicker()
        }
    }

    private fun cargarConfiguraion() {
        val tiempoConfigurado = sharedPreferences.getString("TIME_REMINDER", null)
        if(tiempoConfigurado != null){
            mostrarHora.setText("Hora configurada previamente: " + tiempoConfigurado + "\n\nPresiona acá para configurar una nueva hora")
        }
    }
    private fun inicializarUI(){
        cerrarBtn = findViewById(R.id.cerrar_modal)
        mostrarHora = findViewById(R.id.configurarHora_et)
        establecerBtn = findViewById(R.id.establecer_btn)
        cancelarBtn = findViewById(R.id.cancelar_btn)

        mostrarHora.isFocusable = false
        mostrarHora.isFocusableInTouchMode = false


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.INTERNET, Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "DSMChannel"
            val description = "Canal de recordatorios"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("DSM", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun mostrarTimePicker(){
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Selecciona la hora de recordatorio")
            .build()

        timePicker.show(supportFragmentManager, "DSM")
        timePicker.addOnPositiveButtonClickListener{
            mostrarHora.setError(null)
            val tiempo: String
            if(timePicker.hour > 12){
                tiempo = String.format("%02d", timePicker.hour - 12) + ":" + String.format("%02d", timePicker.minute) + "PM"
            }
            else{
                tiempo = String.format("%02d", timePicker.hour) + ":" + String.format("%02d", timePicker.minute) + "AM"
            }
            tiempoActual = tiempo
            mostrarHora.setText("Hora configurada: " + tiempo + "\n\nPresiona acá para configurar una nueva hora")

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }
    private fun setAlarm(){
        if(!::calendar.isInitialized)
        {
            mostrarHora.setError("Debe seleccionar una hora")
            Toast.makeText(this,"Debe seleccionar una hora", Toast.LENGTH_LONG).show();
            return
        }
        mostrarHora.setError(null)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RecordatorioReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
        val editor = sharedPreferences.edit()
        editor.putString("TIME_REMINDER", tiempoActual)
        editor.apply()
        Toast.makeText(this, "Recordatorio configurado con éxito", Toast.LENGTH_SHORT).show()
    }
    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RecordatorioReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        val editor = sharedPreferences.edit()
        editor.remove("TIME_REMINDER")
        editor.apply()
        mostrarHora.setText(null)
        Toast.makeText(this, "Recordatorio cancelado con éxito", Toast.LENGTH_SHORT).show()
    }
}