package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Slot


class TimeSlotDetailsUserFragment : Fragment(R.layout.fragment_time_slot_details) {
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
    private val vm: ProfileViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var id : String
    private var titleSlot = ""
    private var chosenSlot =  Slot("","","","","",-1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Details advertisement")
        //Log.i("test!!!", "prima $pos")
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

        // Log.i("test", "dopo iniz $pos")
        vm.email.observe(this.viewLifecycleOwner) {
            id = it
        }
        vm.slot.observe(this.viewLifecycleOwner) {
            titleSlot = it
            Log.i("test_edit", titleSlot)

            if (titleSlot != "") {
                readData(id)
                Log.i("test_edit", "entra?" + chosenSlot)
            }

        }
        Log.i("test_edit", "entra?" + chosenSlot)

        edit_button = view.findViewById(R.id.editButton)
        edit_button.isClickable = true
        edit_button.setOnClickListener {
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(TimeSlotEditFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimeSlotEditFragment>(R.id.myNavHostFragment)
                //findNavController().navigate(R.id.action_timeSlotDetailsFragment_to_timeSlotEditFragment)
            }
        }

    }

    private fun readData(id: String){
        Log.i("test_edit", "db?" + id)
        db.collection("slots").document(id).get().addOnSuccessListener {
            Log.i("test_edit", "db?" + titleSlot)
            if (it.exists()) {
                it.data!!.forEach { (c, s) ->
                    s as HashMap<*, *>

                    if (s["title"].toString() == titleSlot) {

                        chosenSlot = Slot(
                            s["title"].toString(),
                            s["description"].toString(),
                            s["date"].toString(),
                            s["duration"].toString(),
                            s["location"].toString(),
                            s["pos"].toString().toInt()
                        )
                        title_field.text = chosenSlot.title
                        description_field.text = chosenSlot.description
                        date_field.text = chosenSlot.date
                        duration_field.text = chosenSlot.duration
                        location_field.text = chosenSlot.location

                    }

                }
            }
            Log.i("test_edit", "prova?" + chosenSlot)
        }

    }
}

