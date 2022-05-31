package it.polito.lab4.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ViewModel
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Slot


class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    var title: String = "Title"
    var description: String = "Description"
    var date: String = "Date"
    var duration: String = "Duration"
    var location: String = "Location"
    var hours: String = "Cost"
    lateinit var edit_button: Button
    lateinit var copied_button: Button
    lateinit var show_prof_button: Button
    lateinit var chat_button: Button

    lateinit var title_field: TextView
    lateinit var description_field: TextView
    lateinit var date_field: TextView
    lateinit var duration_field: TextView
    lateinit var location_field: TextView
    lateinit var hours_field: TextView
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var id: String
    private lateinit var slot: Slot


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_time_slot_details, container, false)
        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle("Details advertisement")
        //Log.i("test!!!", "prima $pos")
        title_field = view.findViewById(R.id.title)
        description_field = view.findViewById(R.id.description)
        duration_field = view.findViewById(R.id.duration)
        location_field = view.findViewById((R.id.location))
        date_field = view.findViewById(R.id.date)
        hours_field = view.findViewById(R.id.hours)

        title = title_field.text.toString()
        description = description_field.text.toString()
        date = date_field.text.toString()
        duration = duration_field.text.toString()
        location = location_field.text.toString()
        hours = hours_field.text.toString()

        show_prof_button = view.findViewById(R.id.show_prof_button)
        edit_button = view.findViewById(R.id.editButton)
        copied_button = view.findViewById(R.id.editButtonCopied)
        chat_button = view.findViewById(R.id.openChatButton)

        Log.i("test_details", "Time slot details")
        // Log.i("test", "dopo iniz $pos")
        vm.email.observe(this.viewLifecycleOwner) {
            id = it
        }
        vm.slot.observe(this.viewLifecycleOwner) {
            slot = it
             Log.i("test_details", slot.toString())
            if (slot.user == id) {
                show_prof_button.visibility = View.GONE
                show_prof_button.isClickable = false
                chat_button.visibility = View.GONE
                chat_button.isClickable = false
                if(slot.status.equals("Available")) {
                    edit_button.visibility = View.VISIBLE
                    edit_button.isClickable = true
                    copied_button.visibility = View.VISIBLE
                    copied_button.isClickable = true
                }else{
                    edit_button.visibility = View.GONE
                    edit_button.isClickable = false
                    copied_button.visibility = View.VISIBLE
                    copied_button.isClickable = true
                }
            } else {
                edit_button.visibility = View.GONE
                edit_button.isClickable = false
                copied_button.visibility = View.GONE
                copied_button.isClickable = false
                show_prof_button.isClickable = true
                show_prof_button.visibility = View.VISIBLE
                if(slot.status.equals("Available")) {
                    //user can open the chat only if the slot is available
                    chat_button.visibility = View.VISIBLE
                    chat_button.isClickable = true
                }else{
                    chat_button.visibility = View.GONE
                    chat_button.isClickable = false

                }
            }
            if (slot.title != "") {
                Log.i("test_edit", "entra? ${slot.title}")
                title_field.text = slot.title
                description_field.text = slot.description
                date_field.text = slot.date
                duration_field.text = slot.duration
                location_field.text = slot.location
                hours_field.text = "Cost: ${slot.hours}"
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

        copied_button.setOnClickListener {
            slot.id = ""
            vm.setSlot(slot)
            Log.i("testDetails", slot.toString())
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

        chat_button.setOnClickListener {
            //Toast.makeText(this.context,"OPEN CHAT",Toast.LENGTH_LONG).show()
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(ShowProfileFragment::class.toString())
                setReorderingAllowed(true)
                replace<ChatFragment>(R.id.myNavHostFragment)
            }


            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
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
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Title", title_field.text.toString())
        outState.putString("Description", description_field.text.toString())
        outState.putString("Date", date_field.text.toString())
        outState.putString("Location", location_field.text.toString())
        outState.putString("Duration", duration_field.text.toString())
        outState.putString("Hours", hours_field.text.toString())

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            title_field.setText(savedInstanceState.getString("Title","0"))
            description_field.setText(savedInstanceState.getString("Description","0"))
            date_field.setText(savedInstanceState.getString("Date","0"))
            location_field.setText(savedInstanceState.getString("Location","0"))
            duration_field.setText(savedInstanceState.getString("Duration","0"))
            hours_field.setText(savedInstanceState.getString("Hours","0"))

        }
    }



}

