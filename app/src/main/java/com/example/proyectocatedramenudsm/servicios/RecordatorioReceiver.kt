package com.example.proyectocatedramenudsm.servicios

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.proyectocatedramenudsm.MainActivity
import com.example.proyectocatedramenudsm.R
import com.example.proyectocatedramenudsm.RecordatorioActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.StringBuilder
import java.security.Permission
import java.text.NumberFormat
import java.util.Locale

class RecordatorioReceiver: BroadcastReceiver() {
    private val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    override fun onReceive(context: Context?, intent: Intent?) {
        cargarDatos(context, intent)
    }

    private fun mostrarNotificacion(context: Context?, intent: Intent?, mensaje: String){
        val i = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText(mensaje)

        val builder = NotificationCompat.Builder(context!!, "DSM")
            .setSmallIcon(R.drawable.ic_menu)
            .setContentTitle("MENÚ DEL DÍA")
            .setContentText(mensaje)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(bigTextStyle)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("DSM", "Error en permiso")
            return
        }
        notificationManager.notify(123, builder.build())
    }
    private fun cargarDatos(context: Context?, intent: Intent?){
        val database = FirebaseDatabase.getInstance()
        val almuerzosRef = database.getReference("Almuerzos")

        almuerzosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var platos = StringBuilder()
                var count = 0
                for (platoSnapshot in snapshot.child("Platos").children) {
                    val platoMap = platoSnapshot.getValue() as Map<String, Any>
                    platos.appendLine("Plato #${++count}: ${platoSnapshot.key.toString()} (${currencyFormatter.format(platoMap["Precio"] as Double)})")
                }
                mostrarNotificacion(context, intent, platos.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Plato", "Error: " + error.message)
            }
        })
    }
}