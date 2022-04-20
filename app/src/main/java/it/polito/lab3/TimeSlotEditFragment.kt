package it.polito.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.editButton1)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_timeEditFragment_to_timeSlotFragment)
        }
    }
}