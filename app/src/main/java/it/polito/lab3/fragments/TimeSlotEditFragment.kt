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
        private val ARG = "Posi"
        fun newInstance(pos: Int): TimeSlotEditFragment {
            val args: Bundle = Bundle()
            Log.i("test", "AAAAAAAAAAAAA $pos")
            args.putInt(ARG, pos)
            val fragment = TimeSlotEditFragment()
            fragment.arguments = args
            Log.i("test??", fragment.requireArguments().getInt("Posi", -1).toString())
            return fragment
        }
    }
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
    private  var eliminare = Slot("","","","","")

    //val vm by viewModels<TimeSlotViewModel>()
    private val timeSlotViewModel: TimeSlotViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: Bundle = Bundle()
        args.putInt("inutile", 5)
        this.arguments = args
        val pos = requireArguments().getInt("Position", 10000)
        //Log.i("test!!!", "prima $pos")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val posi = this.arguments?.getInt("Posi", 0)
        Log.i("Test", "SUSA $posi")
        activity?.setTitle("Edit time slot")
        Log.i("Test", "SUSA")
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
            if( posi!= 0){
                Log.i("test!!!", "Dentroo")
                if (it.isNotEmpty()) {
                    val slotList = it
                    eliminare = slotList[posi!!]
                    if(eliminare.title!= ""){
                        title_field.setText(eliminare.title)
                    }
                    if(eliminare.description!=""){
                        description_field.setText(eliminare.description)
                    }
                    if(eliminare.date!= ""){
                        date_field.setText(eliminare.date)
                    }
                    if(eliminare.duration!= ""){
                        from_field.setText(eliminare.duration.split("-")[0])
                        to_field.setText(eliminare.duration.split("-")[1])
                    }
                    if(eliminare.location!= ""){
                        location_field.setText(eliminare.location)
                    }
            }


            }

        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(title_field.text.toString()!= "Title"){
                    title = title_field.text.toString()
                }

                if(description_field.text.toString()!= "Description"){
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

                if(eliminare.title!=""){
                    timeSlotViewModel.remove(eliminare)
                }
                    var new = Slot(title,description, date, "$from-$to", location)
                    Log.i("test",new.toString())
                    if(title != "" && description != ""){
                        timeSlotViewModel.setSlot(new)

                }


               // this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.commit {
                    var fragment = TimeSlotListFragment()
                    addToBackStack(fragment::class.toString())
                    setReorderingAllowed(true)
                    replace(R.id.myNavHostFragment, fragment)
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




