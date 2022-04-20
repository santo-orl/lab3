package it.polito.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    // TODO: Rename and change types of parameters
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







    private fun editAdv(): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("Title", "0")
            description = savedInstanceState.getString("Description", "0")
            date = savedInstanceState.getString("Date", "0")
            duration= savedInstanceState.getString("Time", "0")
            location = savedInstanceState.getString("Location", "0")
        }
        title_field = view.findViewById<TextView>(R.id.title)
        description_field= view.findViewById<TextView>(R.id.description)
        date_field = view.findViewById<TextView>(R.id.date)
        duration_field = view.findViewById<TextView>(R.id.duration)
        location_field = view.findViewById<TextView>(R.id.location)

        title_field.text = title
        description_field.text = description
        date_field.text = date
        duration_field.text = duration
        location_field.text = location

        edit_button = view.findViewById(R.id.editButton)
        edit_button.setOnClickListener {
            findNavController().navigate(R.id.action_timeSlotDetailsFragment_to_timeSlotEditFragment)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("Title", title)
        outState.putString("Description", description)
        outState.putString("Date", date)
        outState.putString("Time", duration)
        outState.putString("Location", location)

        //  outState.putParcelable("Skills", recycler.layoutManager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
        //outState.putString("Skills", skills)
    }



}