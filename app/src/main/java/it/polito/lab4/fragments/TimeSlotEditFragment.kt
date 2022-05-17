package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ViewModel
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Slot


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
    private  var eliminare = Slot("", "", "", "", "", 0,"")

    //val vm by viewModels<TimeSlotViewModel>()
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var id : String
    private lateinit var slot: Slot

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Edit time slot")

        title_field = view.findViewById(R.id.editTitle)
        description_field = view.findViewById(R.id.editDescription)
        date_field = view.findViewById(R.id.date_edit)
        location_field = view.findViewById((R.id.location_edit))
        date_field.showSoftInputOnFocus = false

        from_field = view.findViewById(R.id.from_edit)
        from_field.showSoftInputOnFocus = false

        to_field = view.findViewById(R.id.to_edit)
        to_field.showSoftInputOnFocus = false

        vm.email.observe(this.viewLifecycleOwner) {
            id = it
        }
        vm.slot.observe(this.viewLifecycleOwner){
             slot = it
            if(slot.title!= ""){
                title_field.setText(slot.title)
                description_field.setText(slot.description)
                date_field.setText(slot.date)
                from_field.setText(slot.duration.split("-")[0])
                to_field.setText(slot.duration.split("-")[1])
                location_field.setText(slot.location)
                Log.i("test_edit", "Entra")

            }else{
                vm.skill.observe(this.viewLifecycleOwner){ tit->
                    if(tit!= ""){
                        title_field.setText(tit)
                        title_field.isClickable = false
                    }
                }
                vm.description.observe(this.viewLifecycleOwner){ desc ->
                    if(desc!= ""){
                        description_field.setText(desc)
                        description_field.isClickable = false
                    }
                }
                from_field.setHint(from)
                to_field.setHint(to)
                location_field.setHint(location)
                date_field.setHint(date)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                title = title_field.text.toString()
                description = description_field.text.toString()

                if(date_field.text.toString()!= ""){
                    date = date_field.text.toString()
                }else{
                    Toast.makeText(context, "All the fields must be completed", Toast.LENGTH_SHORT).show()
                    return
                }
                if(from_field.text.toString()!= ""){
                    from = from_field.text.toString()
                }else{
                    Toast.makeText(context, "All the fields must be completed", Toast.LENGTH_SHORT).show()
                    return
                }
                if(to_field.text.toString()!= ""){
                    to = to_field.text.toString()
                }else{
                    Toast.makeText(context, "All the fields must be completed", Toast.LENGTH_SHORT).show()
                    return
                }
                if(location_field.text.toString()!= ""){
                    location = location_field.text.toString()
                }else{
                    Toast.makeText(context, "All the fields must be completed", Toast.LENGTH_SHORT).show()
                    return
                }

                vm.slot.observe(viewLifecycleOwner){
                    if (title != slot.title || description != slot.description || date != slot.date ||
                            "$from-$to" != slot.duration || location != slot.location){
                        var new = Slot(title, description, date, "$from-$to", location, 0,id)
                        Log.i("test",new.toString())
                        if(title != "" && description != ""){
                            vm.addSlot(new)
                    }
                }

                    /*var new = Slot(title, description, date, "$from-$to", location, 0,id)
                    Log.i("test",new.toString())
                    if(title != "" && description != ""){
                        vm.addSlot(new)*/
                }


               // this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.commit {
                    addToBackStack(TimeSlotUserListFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<TimeSlotUserListFragment>(R.id.myNavHostFragment)
                }
               //findNavController().navigate(R.id.action_timeSlotEditFragment_to_containerFragment)
                //this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
            }
        })

        date_field.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            // we have to implement setFragmentResultListener
            setFragmentResultListener("KeyDate") { _, bundle ->
                val result = bundle.getString("SELECTED_DATE")
                date_field.setText(result)
            }
            // show
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")


        }
        from_field.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            setFragmentResultListener("KeyTime") { _, bundle ->
                val result1 = bundle.getString("TIME")
                from_field.setText(result1)
            }
            timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
        }

        to_field.setOnClickListener {

            val timePickerFragment = TimePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            setFragmentResultListener("KeyTime") { _, bundle ->
                val result1 = bundle.getString("TIME")
                to_field.setText(result1)

            }
            timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
        }
    }

  /*  private fun readData(id: String) {
        db.collection("skills").document(id).collection("slots")
            .whereEqualTo("title", slot.value?.title)
            .whereEqualTo("description",slot.value?.description)
            .whereEqualTo("location",slot.value?.location)
            .whereEqualTo("date",slot.value?.date)
            .whereEqualTo("duration",slot.value?.duration).get().addOnSuccessListener {
                    result ->
                for (document in result) {
                    val s = document.data as HashMap<*, *>
                    Log.i("TEST", "${document.id} + ${document.data}  ")

                    slotList.add(
                        Slot(
                            s["title"].toString(),
                            s["description"].toString(),
                            s["date"].toString(),
                            s["duration"].toString(),
                            s["location"].toString(),
                            slotList.size,
                            s["user"].toString()
                        )
                    )
                        title_field.setText(chosenSlot.title)
                        description_field.setText(chosenSlot.description)
                        date_field.setText(chosenSlot.date)
                        from_field.setText(chosenSlot.duration.split("-")[0])
                        to_field.setText(chosenSlot.duration.split("-")[1])
                        location_field.setText(chosenSlot.location)
                    }
                }
            }
        }
    }
*/

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




