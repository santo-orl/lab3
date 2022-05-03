package it.polito.lab3.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel


class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    var position: Int = 0
    var title: String = "Title"
    var description: String = "Description"
    var date: String = "Date"
    var duration: String = "Duration"
    var location: String = "Location"
    lateinit var edit_button: Button

    lateinit var title_field: TextView
    lateinit var description_field: TextView
    lateinit var date_field: TextView
    lateinit var duration_field: TextView
    lateinit var location_field: TextView

    //val vm by viewModels<TimeSlotViewModel>()
    //START FRAGMENT
    private val timeSlotViewModel: TimeSlotViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title_field = view.findViewById(R.id.title)
        description_field = view.findViewById(R.id.description)
        duration_field = view.findViewById(R.id.duration)
        location_field = view.findViewById((R.id.location))
        date_field = view.findViewById(R.id.date)

        title = title_field.text.toString()
        description = description_field.text.toString()
        date = date_field.text.toString()
        duration = duration_field.text.toString()
        location = location_field.text.toString()

        timeSlotViewModel.slots.observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                var slotList = it
                Log.i("test", slotList[0].toString())
                title_field.text = slotList[position].title
                description_field.text = slotList[position].description
                date_field.text = slotList[position].date
                duration_field.text = slotList[position].duration
                location_field.text = slotList[position].location
            }



            edit_button = view.findViewById(R.id.editButton)
            edit_button.setOnClickListener {
                findNavController().navigate(R.id.timeSlotEditFragment)
            }
        }

    }
}
