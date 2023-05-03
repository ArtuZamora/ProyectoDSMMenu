package com.example.proyectocatedramenudsm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.proyectocatedramenudsm.fragmentos.TimePickerFragment
import com.example.proyectocatedramenudsm.servicios.RecordatorioReceiver

class RecordatorioActivity : AppCompatActivity() {
    private lateinit var cerrarBtn: ImageView
    private lateinit var timePicker: EditText
    private lateinit var establecerBtn: Button
    private lateinit var cancelarBtn: Button
    private lateinit var timePickerFragment: TimePickerFragment
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordatorio)
        inicializarUI()
        createNotificationChannel()

        timePicker.setOnClickListener { showTimePickerDialog() }
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
        }
    }


    private fun inicializarUI(){
        cerrarBtn = findViewById(R.id.cerrar_modal)
        timePicker = findViewById(R.id.timePicker_hora)
        establecerBtn = findViewById(R.id.establecer_btn)
        cancelarBtn = findViewById(R.id.cancelar_btn)
    }

    private fun showTimePickerDialog() {
        timePickerFragment = TimePickerFragment { onTimeSelected(it) }
        timePickerFragment.show(supportFragmentManager, "timePicker")
    }
    private fun onTimeSelected(time: String) {
        timePicker.setText("Recordatorio configurado a las $time h.\n\n Presiona acá para configurar nueva hora")
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "DSMChannel"
            val description = "Channel for reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("DSM", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RecordatorioReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP, timePickerFragment.calendar.timeInMillis,
            pendingIntent
        )

        Log.e("DSM", "Tiempo en millis: ${timePickerFragment.calendar.timeInMillis}")
        Toast.makeText(this, "Recordatorio configurado con éxito", Toast.LENGTH_SHORT).show()
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RecordatorioReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Recordatorio cancelado con éxito", Toast.LENGTH_SHORT).show()
    }
}