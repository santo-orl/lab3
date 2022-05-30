package it.polito.lab4.fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.text.DateFormat
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.lang.reflect.Field
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

        return TimePickerDialog(requireActivity(), android.R.style.Theme_Holo_Light_Dialog,this, hour ,minute,true)
    }

    override fun onTimeSet(view: TimePicker, hour: Int, minute: Int) {
        Log.i("time", "PROVA")
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        setTimePickerInterval(view)
        val selectedTimeBundle = Bundle()
        val selectedTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)
        Log.i("time", selectedTime)
        selectedTimeBundle.putString("TIME", selectedTime)
        setFragmentResult("KeyTime", selectedTimeBundle)

    }
    @SuppressLint("NewApi", "PrivateApi")
    private fun setTimePickerInterval(timePicker: TimePicker) {
        val TIME_PICKER_INTERVAL = 15
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            // Field timePickerField = classForid.getField("timePicker");
            val field: Field = classForid.getField("minute")
            var minutePicker = timePicker.findViewById(field.getInt(null)) as NumberPicker
            minutePicker.setMinValue(0)
            minutePicker.setMaxValue(7)
            var displayedValues = ArrayList<String>()
            run {
                var i = 0
                while (i < 60) {
                    displayedValues.add(String.format("%02d", i))
                    i += TIME_PICKER_INTERVAL
                }
            }
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.setDisplayedValues(
                displayedValues
                    .toArray(arrayOfNulls<String>(0))
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}