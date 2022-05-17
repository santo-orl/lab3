package it.polito.lab4.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // default date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val c = DatePickerDialog(requireActivity(), this, year, month, day)
        c.datePicker.minDate
        // return new DatePickerDialog instance
        return c
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        //converto il simpleDateFormat in Calendar
        //confronto se la data scelta è antecedente/conseguente oggi
        var selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val date2 = selectedDate.format(calendar.time)
        val cal = Calendar.getInstance()
        cal.time = selectedDate.parse(selectedDate.format(cal.time))
        if (!cal.before(calendar)) {
            Toast.makeText(this.context, "You cannot create slot in the past", Toast.LENGTH_SHORT)
                .show()
        } else {
            val selectedDateBundle = Bundle()
            selectedDateBundle.putString("SELECTED_DATE", date2)

            setFragmentResult("KeyDate", selectedDateBundle)
        }
    }
}