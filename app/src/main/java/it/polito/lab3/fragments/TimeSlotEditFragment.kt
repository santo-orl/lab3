package it.polito.lab3.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.timeSlots.Slot
import kotlinx.android.synthetic.main.activity_show_profile.*


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    private  var date=  "Date"
    private  var from= "From"
    private  var to= "To"
    private  var title = "Title"
    private  var description= "Description"
    private  var location= "Location"

    private lateinit var date_field: EditText
    private lateinit var from_field: EditText
    private lateinit var to_field: EditText
    private lateinit var title_field: EditText
    private lateinit var description_field: EditText
    private lateinit var location_field: EditText
    //val vm by viewModels<TimeSlotViewModel>()
    private val timeSlotViewModel: TimeSlotViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_field = view.findViewById(R.id.editTitle)
        description_field = view.findViewById(R.id.editDescription)
        date_field = view.findViewById(R.id.date_edit)
        location_field = view.findViewById((R.id.location_edit))
        date_field.showSoftInputOnFocus = false

        from_field = view.findViewById(R.id.from_edit)
        from_field.showSoftInputOnFocus = false

        to_field = view.findViewById(R.id.to_edit)
        to_field.showSoftInputOnFocus = false


     /*   timeSlotViewModel.slots.observe(this.viewLifecycleOwner){
            if(it != "" && it!= title) {
                title_field.setText(it.toString())
            }
        }*/

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                title = title_field.text.toString()
                description = description_field.text.toString()
                date = date_field.text.toString()
                from = from_field.text.toString()
                to = to_field.text.toString()
                location = location_field.text.toString()

                var new = Slot(title,description, date, "$from-$to", location)
                Log.i("test",new.toString())
                timeSlotViewModel.setSlot(new)

               //findNavController().navigate(R.id.action_timeSlotEditFragment_to_containerFragment)
                this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
            }

        })

        date_field.setOnClickListener {
           findNavController().navigate(R.id.action_timeSlotEditFragment_to_datePickerFragment)
            setFragmentResultListener("KeyDate") { _, bundle ->
                val result = bundle.getString("SELECTED_DATE")
                date_field.setText(result)
            }

        }
        from_field.setOnClickListener {
            //findNavController().navigate(R.id.action_timeSlotEditFragment_to_timePickerFragment)
            val supportFragmentManager = requireActivity().supportFragmentManager

            TimePickerFragment().show(supportFragmentManager,"TimePicker")
            TimePickerFragment().show(supportFragmentManager,"TimePicker")
            supportFragmentManager.setFragmentResultListener("KeyTime",
                viewLifecycleOwner) { _, bundle ->
                val result1 = bundle.getString("FROM_TIME")
                from_field.setText(result1)
                val result = bundle.getString("TO_TIME")
                to_field.setText(result)
            }

        }

        to_field.setOnClickListener {
            val supportFragmentManager = requireActivity().supportFragmentManager
            TimePickerFragment().show(supportFragmentManager,"TimePicker")
            supportFragmentManager.setFragmentResultListener("KeyTime",
                viewLifecycleOwner) { _, bundle ->
                val result = bundle.getString("TO_TIME")
                to_field.setText(result)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.timeSlotDetailsFragment)
        return true
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




