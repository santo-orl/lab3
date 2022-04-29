package it.polito.lab3.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import it.polito.lab3.R


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    private lateinit var date_text: EditText
    private lateinit var from_text: EditText
    private lateinit var to_text: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date_text = view.findViewById(R.id.date_edit)
        date_text.showSoftInputOnFocus = false

        from_text = view.findViewById(R.id.from_edit)
        from_text.showSoftInputOnFocus = false

        to_text = view.findViewById(R.id.to_edit)
        to_text.showSoftInputOnFocus = false

        date_text.setOnClickListener {
           findNavController().navigate(R.id.action_timeSlotEditFragment_to_datePickerFragment)
            setFragmentResultListener("KeyDate") { _, bundle ->
                val result = bundle.getString("SELECTED_DATE")
                date_text.setText(result)
            }

        }
        from_text.setOnClickListener {
            //findNavController().navigate(R.id.action_timeSlotEditFragment_to_timePickerFragment)
            val supportFragmentManager = requireActivity().supportFragmentManager

            TimePickerFragment().show(supportFragmentManager,"TimePicker")
            TimePickerFragment().show(supportFragmentManager,"TimePicker")
            supportFragmentManager.setFragmentResultListener("KeyTime",
                viewLifecycleOwner) { _, bundle ->
                val result1 = bundle.getString("FROM_TIME")
                from_text.setText(result1)
                val result = bundle.getString("TO_TIME")
                to_text.setText(result)
            }

        }

        to_text.setOnClickListener {
            val supportFragmentManager = requireActivity().supportFragmentManager
            TimePickerFragment().show(supportFragmentManager,"TimePicker")
            supportFragmentManager.setFragmentResultListener("KeyTime",
                viewLifecycleOwner) { _, bundle ->
                val result = bundle.getString("TO_TIME")
                to_text.setText(result)
            }
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
        }*/



