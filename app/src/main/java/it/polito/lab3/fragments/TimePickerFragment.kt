package it.polito.lab3.fragments


import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
    private var from:String = ""
    private var to:String = ""

    private lateinit var sharedPref:SharedPreferences
    private val sharedPrefFIle = "it.polito.fragment"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreateDialog(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)
        // default date
        if (sharedPref.getString("FROM_TIME","").toString() != "") {
            Log.d("testing from2",from)
            from = sharedPref.getString("FROM_TIME","").toString()
            if (sharedPref.getString("TO_TIME","").toString() != "") {
                to = sharedPref.getString("TO_TIME","").toString()
            }
            hour =from.split(":")[0].toInt()
            minute = from.split(":")[1].toInt()
            Log.d("testing create",from)
        }else{
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
        }
        return TimePickerDialog(requireActivity(), this, hour ,minute,true)
    }

 /* override fun onSaveInstanceState(outState: Bundle) {
        Log.d("testing_saved", "")
        outState.putString("FROM_TIME",from)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            Log.d("testing", savedInstanceState.getString("FROM_TIME", ""))
            from = savedInstanceState.getString("FROM_TIME", "")
        }
    }*/

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        Log.d("testing on time","")
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val selectedTimeBundle = Bundle()
        val selectedTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)
        if(from != ""){
            Log.d("testing on time1",from)
            selectedTimeBundle.putString("FROM_TIME", from)
            selectedTimeBundle.putString("TO_TIME", selectedTime)
            to = selectedTime
        }else{
            Log.d("testing on time2",from)
            selectedTimeBundle.putString("FROM_TIME", selectedTime)
            from = selectedTime
        }
        Log.d("testing on time3",from)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("FROM_TIME", from)
        editor.putString("TO_TIME", to)
        editor.apply()
        setFragmentResult("KeyTime", selectedTimeBundle)

    }
}