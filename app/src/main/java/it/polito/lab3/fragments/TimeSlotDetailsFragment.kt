package it.polito.lab3.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.timeSlots.Adapter_frgTime


class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    companion object {
        private val ARG = "Position"
        fun newInstance(pos: Int): TimeSlotDetailsFragment {
            val args: Bundle = Bundle()
            Log.i("test", "EEEEEEEEEEEEEEEE $pos")
            args.putInt(ARG, pos)
            val fragment = TimeSlotDetailsFragment()
            fragment.arguments = args
            Log.i("test", fragment.requireArguments().getInt("Position", 10000).toString())
            return fragment
        }
    }
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

    private val timeSlotViewModel: TimeSlotViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pos = requireArguments().getInt("Position", 10000)
        Log.i("test!!!", "prima $pos")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pos = requireArguments().getInt("Position", 10000)
        Log.i("test!!!", "prima $pos")
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

        Log.i("test", "dopo iniz $pos")

        timeSlotViewModel.slots.observe(this.viewLifecycleOwner) {
            Log.i("test", "observe $pos")
            if (it.isNotEmpty()) {
                Log.i("test", "ENRA")
                val slotList = it
                    Log.i("test3", "position $pos $")
                    title_field.text = slotList[pos!!].title
                    description_field.text = slotList[pos].description
                    date_field.text = slotList[pos].date
                    duration_field.text = slotList[pos].duration
                    location_field.text = slotList[pos].location

            }
            }

            edit_button = view.findViewById(R.id.editButton)
            edit_button.setOnClickListener {
                findNavController().navigate(R.id.action_timeSlotDetailsFragment_to_timeSlotEditFragment)
            }

        }
    }

