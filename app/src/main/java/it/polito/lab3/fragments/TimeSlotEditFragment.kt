package it.polito.lab3.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.timeSlots.Slot
import kotlinx.android.synthetic.main.activity_show_profile.*


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    companion object {
        private val ARG = "Position)"
        fun newInstance(pos: Int): TimeSlotEditFragment {
            val args: Bundle = Bundle()
            Log.i("test", "AAAAAAAAAAAAA $pos")
            args.putInt(ARG, pos)
            val fragment = TimeSlotEditFragment()
            fragment.arguments = args
            Log.i("test??", fragment.requireArguments().getInt("Position)", -1).toString())
            return fragment
        }
    }
    private  var date=  "Date"
    private  var from= "From"
    private  var to= "To"
    private  var title = "Title"
    private  var description= "Description"
    private  var location= "Location"
    var count = 0

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
        val args: Bundle = Bundle()
        args.putInt("inutile", 5)
        this.arguments = args

        activity?.setTitle("Edit time slot")

        Log.i("Test", "SUSA")
        var  pos = requireArguments().getInt("Position)", -1)
            Log.i("Test", "SUSA $pos")

        title_field = view.findViewById(R.id.editTitle)
        description_field = view.findViewById(R.id.editDescription)
        date_field = view.findViewById(R.id.date_edit)
        location_field = view.findViewById((R.id.location_edit))
        date_field.showSoftInputOnFocus = false

        from_field = view.findViewById(R.id.from_edit)
        from_field.showSoftInputOnFocus = false

        to_field = view.findViewById(R.id.to_edit)
        to_field.showSoftInputOnFocus = false

        timeSlotViewModel.slots.observe(this.viewLifecycleOwner) {
            if( pos!= -1){
                Log.i("test!!!", "Dentroo")
                if (it.isNotEmpty()) {
                    val slotList = it
                    if(slotList[pos].title!= ""){
                        title_field.setText(slotList[pos].title)
                    }
                    if(slotList[pos].description!=""){
                        description_field.setText(slotList[pos].description)
                    }
                    if(slotList[pos].date!= ""){
                        date_field.setText(slotList[pos].date)
                    }
                    if(slotList[pos].duration!= ""){
                        from_field.setText(slotList[pos].duration.split("-")[0])
                        to_field.setText(slotList[pos].duration.split("-")[1])
                    }
                    if(slotList[pos].location!= ""){
                        location_field.setText(slotList[pos].location)
                    }
            }


            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(title_field.text.toString()!= ""){
                    title = title_field.text.toString()
                }

                if(description_field.text.toString()!= ""){
                    description = description_field.text.toString()
                }
                if(date_field.text.toString()!= ""){
                    date = date_field.text.toString()
                }
                if(from_field.text.toString()!= ""){
                    from = from_field.text.toString()
                }
                if(to_field.text.toString()!= ""){
                    to = to_field.text.toString()
                }
                if(location_field.text.toString()!= ""){
                    location = location_field.text.toString()
                }

                var new = Slot(title,description, date, "$from-$to", location)
                Log.i("test",new.toString())
                if(title != "" && description != ""){
                    timeSlotViewModel.setSlot(new)
                }

                this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
               /* activity?.supportFragmentManager?.commit {
                    addToBackStack(TimeSlotListFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<TimeSlotListFragment>(R.id.myNavHostFragment)
                }*/
               //findNavController().navigate(R.id.action_timeSlotEditFragment_to_containerFragment)
                //this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
            }
        })

        date_field.setOnClickListener {
           /* activity?.supportFragmentManager?.commit {
                addToBackStack(DatePickerFragment::class.toString())
                setReorderingAllowed(true)
                replace<DatePickerFragment>(R.id.myNavHostFragment)
            }*/
            findNavController().navigate(R.id.action_timeSlotEditFragment_to_datePickerFragment)
            setFragmentResultListener("KeyDate") { _, bundle ->
                val result = bundle.getString("SELECTED_DATE")
                date_field.setText(result)
            }

        }
        from_field.setOnClickListener {
          /*  activity?.supportFragmentManager?.commit {
                addToBackStack(TimePickerFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimePickerFragment>(R.id.myNavHostFragment)
            }*/
            findNavController().navigate(R.id.action_timeSlotEditFragment_to_timePickerFragment)
            setFragmentResultListener("KeyTime") { _, bundle ->
                val result1 = bundle.getString("TIME")
                from_field.setText(result1)
            }
        }

        to_field.setOnClickListener {

            findNavController().navigate(R.id.action_timeSlotEditFragment_to_timePickerFragment)
            setFragmentResultListener("KeyTime") { _, bundle ->
                val result1 = bundle.getString("TIME")
                to_field.setText(result1)

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




