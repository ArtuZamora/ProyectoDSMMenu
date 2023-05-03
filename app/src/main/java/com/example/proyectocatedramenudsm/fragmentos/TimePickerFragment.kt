package com.example.proyectocatedramenudsm.fragmentos

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePickerFragment(val listener:(String) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    var hourOfDay: Int = 0
    var minute: Int = 0
    lateinit var calendar: Calendar
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hourOfDay = hourOfDay
        this.minute = minute
        listener("${hourOfDay.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}")
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val picker = TimePickerDialog(activity as Context, this, hour, minute, false)
        return picker
    }
}