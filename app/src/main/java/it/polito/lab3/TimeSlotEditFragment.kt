package it.polito.lab3

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import kotlinx.android.synthetic.main.fragment_time_slot_edit.*


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    lateinit var date_text: EditText
    lateinit var from_text: EditText
    lateinit var to_text: EditText

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    date_text = view.findViewById(R.id.date_edit)
    date_text.showSoftInputOnFocus = false
    date_text.setOnClickListener{
        findNavController().navigate(R.id.action_timeSlotEditFragment_to_datePickerFragment)
        val supportFragmentManager = requireActivity().supportFragmentManager
        // we have to implement setFragmentResultListener
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                date_text.setText(date)
            }
        }
    }

  /*  date_text.setOnClickListener{
            // create new instance of DatePickerFragment
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            // we have to implement setFragmentResultListener
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    date_edit.setText(date)
                }
            }

            // show
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }
    from_text = view.findViewById(R.id.from_edit)
    from_text.setOnClickListener{
            // create new instance of DatePickerFragment
            val timePickerFragment = TimePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            // we have to implement setFragmentResultListener
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val duration = bundle.getString("SELECTED_TIME")
                    from_edit.setText(duration)
                }
            }

            // show
            timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
        }
    to_text = view.findViewById(R.id.to_edit)
    to_text.setOnClickListener{
            // create new instance of DatePickerFragment
            val timePickerFragment = TimePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            // we have to implement setFragmentResultListener
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val duration = bundle.getString("SELECTED_TIME")
                    to_edit.setText(duration)
                }
            }

            // show
            timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
        }*/

}

    }


