package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Slot


class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    var title: String = "Title"
    var description: String = "Description"
    var date: String = "Date"
    var duration: String = "Duration"
    var location: String = "Location"
    lateinit var edit_button: Button
    lateinit var show_prof_button: Button

    lateinit var title_field: TextView
    lateinit var description_field: TextView
    lateinit var date_field: TextView
    lateinit var duration_field: TextView
    lateinit var location_field: TextView
    private val vm: ProfileViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var id : String
    private lateinit var slot: Slot
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

        show_prof_button = view.findViewById(R.id.show_prof_button)
        edit_button = view.findViewById(R.id.editButton)

       // Log.i("test", "dopo iniz $pos")
        vm.email.observe(this.viewLifecycleOwner) {
            id = it
        }
        vm.slot.observe(this.viewLifecycleOwner) {
            slot = it
           // Log.i("test_edit", slot.toString())
            if (slot.user == id){
                show_prof_button.visibility = View.GONE
                show_prof_button.isClickable = false
                edit_button.visibility = View.VISIBLE
                edit_button.isClickable = true

            }else{
                edit_button.visibility = View.GONE
                edit_button.isClickable = false
                show_prof_button.isClickable = true
            }
            if (slot.title != "") {
                Log.i("test_edit", "entra? ${slot.title}" )
                title_field.text = slot.title
                description_field.text = slot.description
                date_field.text = slot.date
                duration_field.text = slot.duration
                location_field.text = slot.location
               // readData(id)
               //
            }

        }
      //  Log.i("test_edit", "entra?" + chosenSlot)

            //edit_button = view.findViewById(R.id.editButton)

            edit_button.setOnClickListener {
                val activity = it.context as? AppCompatActivity
                activity?.supportFragmentManager?.commit {
                    addToBackStack(TimeSlotEditFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<TimeSlotEditFragment>(R.id.myNavHostFragment)
                    //findNavController().navigate(R.id.action_timeSlotDetailsFragment_to_timeSlotEditFragment)
                }
            }

            show_prof_button.setOnClickListener {
                val activity = it.context as? AppCompatActivity
                activity?.supportFragmentManager?.commit {
                    addToBackStack(ShowProfileFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<ShowProfileFragment>(R.id.myNavHostFragment)
                    //findNavController().navigate(R.id.action_timeSlotDetailsFragment_to_timeSlotEditFragment)
                }
            }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@TimeSlotDetailsFragment.activity?.supportFragmentManager?.popBackStack()
                //findNavController().navigate(R.id.action_showProfileFragment_to_homeFragment)


            }
        })
        }

    /*private fun readData(id: String){
        Log.i("test_edit", "db?" + id)
        db.collection("slots").document(id).get().addOnSuccessListener {
           // Log.i("test_edit", "db?" + titleSlot)
            if (it.exists()) {
                it.data!!.forEach { (c, s) ->
                    s as HashMap<*, *>

                    if (s["title"].toString() == slot.title) {

                      val  chosenSlot = Slot(
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
            //Log.i("test_edit", "prova?" + chosenSlot)
        }

    }*/
    }

