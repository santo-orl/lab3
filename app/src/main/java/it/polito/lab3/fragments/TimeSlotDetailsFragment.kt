package it.polito.lab3.fragments

import android.os.Bundle
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
    private val sharedViewModel: TimeSlotViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        edit_button = view.findViewById(R.id.editButton)
        edit_button.setOnClickListener{
            if(sharedViewModel.hasNoTitleSet()){
                sharedViewModel.setTitle("Title")
            }else{
                sharedViewModel.setTitle(title_field.text.toString())
            }
            findNavController().navigate(R.id.timeSlotEditFragment)}
        }

    }
