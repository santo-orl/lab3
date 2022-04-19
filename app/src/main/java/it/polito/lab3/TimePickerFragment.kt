package it.polito.lab3


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // default date

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        // return new DatePickerDialog instance
        return TimePickerDialog(requireActivity(), this, hour,minute,false)
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)


        val selectedTime = SimpleDateFormat(" HH:mm", Locale.ENGLISH).format(calendar.time)

        val selectedTimeBundle = Bundle()
        selectedTimeBundle.putString("SELECTED_TIME", selectedTime)

        setFragmentResult("REQUEST_KEY", selectedTimeBundle)
    }
}