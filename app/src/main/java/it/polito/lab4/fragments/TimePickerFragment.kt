package it.polito.lab4.fragments


import android.app.Dialog
import android.app.TimePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()
    private var hour by Delegates.notNull<Int>()
    private var minute by Delegates.notNull<Int>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        // default date
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireActivity(),this, hour ,minute,true)
        return timePickerDialog
    }


    override fun onTimeSet(view: TimePicker, hour: Int, minute: Int) {

        Log.i("time", "PROVA")
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val selectedTimeBundle = Bundle()
        val selectedTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)
        Log.i("time", selectedTime)
        selectedTimeBundle.putString("TIME", selectedTime)
        setFragmentResult("KeyTime", selectedTimeBundle)

    }

}