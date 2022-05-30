package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ViewModel
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Slot
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.text.SimpleDateFormat
import java.util.*


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {

    private  var date=  "Date"
    private  var from= "From"
    private  var to= "To"
    private  var title = "Title"
    private  var description= "Description"
    private  var location= "Location"
    private lateinit var btn_saveslot: Button
    private lateinit var btn_goback: Button

    private lateinit var date_field: EditText
    private lateinit var from_field: EditText
    private lateinit var to_field: EditText
    private lateinit var title_field: EditText
    private lateinit var description_field: EditText
    private lateinit var location_field: EditText
    private  var eliminare = Slot("", "", "", "", "", 0, "", "", -1)

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
            location_field = view.findViewById(R.id.location_edit)
            btn_saveslot = view.findViewById(R.id.btn_saveslot)
            btn_goback = view.findViewById(R.id.btn_goback)
            date_field.showSoftInputOnFocus = false

            from_field = view.findViewById(R.id.from_edit)
            from_field.showSoftInputOnFocus = false
            from_field.isEnabled = false

            to_field = view.findViewById(R.id.to_edit)
            to_field.showSoftInputOnFocus = false
            to_field.isEnabled = false

            vm.email.observe(this.viewLifecycleOwner) {
                id = it
            }
            vm.slot.observe(this.viewLifecycleOwner) {
                slot = it
                if (slot.title != "") {
                    title_field.setText(slot.title)
                    description_field.setText(slot.description)
                    date_field.setText(slot.date)
                    from_field.setText(slot.duration.split("-")[0])
                    to_field.setText(slot.duration.split("-")[1])
                    location_field.setText(slot.location)
                    Log.i("test_edit", "Entra")
                    title_field.isClickable = false
                    description_field.isClickable = false

                } else {
                    vm.skill.observe(this.viewLifecycleOwner) { tit ->
                        if (tit != "") {
                            title_field.setText(tit)
                            title_field.isClickable = false
                        }
                    }
                    vm.description.observe(this.viewLifecycleOwner) { desc ->
                        if (desc != "") {
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
                btn_saveslot.setOnClickListener {
                    saveInfo()
                            this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
                        }


                btn_goback.setOnClickListener {
                    this@TimeSlotEditFragment.activity?.supportFragmentManager?.popBackStack()
                }

                activity?.onBackPressedDispatcher?.addCallback(
                    viewLifecycleOwner,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            saveInfo()

                            activity?.supportFragmentManager?.commit {
                                addToBackStack(TimeSlotUserListFragment::class.toString())
                                setReorderingAllowed(true)
                                replace<TimeSlotUserListFragment>(R.id.myNavHostFragment)
                            }

                        }
                    })
                var dataTmp: String = ""
                //DATA
                date_field.setOnClickListener {
                    from_field.isEnabled = true

                    val datePickerFragment = DatePickerFragment()
                    val supportFragmentManager = requireActivity().supportFragmentManager

                    // we have to implement setFragmentResultListener
                    setFragmentResultListener("KeyDate") { _, bundle ->
                        val result = bundle.getString("SELECTED_DATE")
                        dataTmp = result.toString()
                        date_field.setText(result)
                    }
                    // show
                    datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
                }

                var c = Calendar.getInstance()
                var fromTmp: String = ""

                from_field.setOnClickListener {
                    to_field.isEnabled = true
                    val timePickerFragment = TimePickerFragment()
                    val supportFragmentManager = requireActivity().supportFragmentManager
                    /*setFragmentResultListener("KeyTime") { _, bundle ->
                val result1 = bundle.getString("TIME")
                from_field.setText(result1)
                timePickerFragment.show(supportFragmentManager, "TimePickerFragment")

            }*/
                    var tmp = dataTmp

                    var selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                    val date2 = selectedDate.format(c.time)

                    var d2 = selectedDate.parse(tmp)
                    val cal = Calendar.getInstance()
                    cal.time = d2

                    setFragmentResultListener("KeyTime") { _, bundle ->
                        var result1 = bundle.getString("TIME")
                        //formato HH:mm
                        var ora = result1.toString().substring(0, 2).toInt()
                        var minuto = result1.toString().substring(3).toInt()
                        fromTmp = result1.toString()
                        //se oggi
                        if (cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
                            && cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                            && cal.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance()
                                .get(Calendar.DAY_OF_MONTH)
                        ) {
                            //aggiungo ora e minuto
                            cal.set(Calendar.HOUR, ora)
                            cal.set(Calendar.MINUTE, minuto)

                            println("ENTRO NEL CASO DELLA STESSA DATA")
                            println(cal)
                            //se data scelta prima di oggi errore
                            if (cal.before(Calendar.getInstance())) {
                                println("ENTRO NEL CASO DATA SCELTA PRIMA DI OGGI")
                                Toast.makeText(
                                    this.context,
                                    "You cannot chose a date before now",
                                    Toast.LENGTH_LONG
                                ).show()
                            }//if
                            //altrimenti va bene
                            else {
                                println("ENTRO NEL CASO DATA VA BENE")
                                result1 = bundle.getString("TIME")
                                println(result1)
                                from_field.setText(result1)
                            }//else
                        }//if
                        else {
                            println("MIAO")
                            result1 = bundle.getString("TIME")
                            from_field.setText(result1)
                        }//else

                    }

                    timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
                }

                to_field.setOnClickListener {

                    val timePickerFragment = TimePickerFragment()
                    val supportFragmentManager = requireActivity().supportFragmentManager
                    setFragmentResultListener("KeyTime") { _, bundle ->
                        val result1 = bundle.getString("TIME")
                        var fromOra = fromTmp.substring(0, 2).toInt()
                        var fromMin = fromTmp.substring(3).toInt()
                        var toOra = result1.toString().substring(0, 2).toInt()
                        var toMin = result1.toString().substring(3).toInt()
                        if (fromOra > toOra || fromOra == toOra && fromMin > toMin) {
                            Toast.makeText(this.context, "wrong date", Toast.LENGTH_SHORT).show()
                        } else {
                            to_field.setText(result1)
                        }
                    }
                    timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
                }

            }

    private fun saveInfo() {
        title = title_field.text.toString()
        description = description_field.text.toString()

        if (date_field.text.toString() != "") {
            date = date_field.text.toString()
        } else {
            Toast.makeText(
                context,
                "All the fields must be completed",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (from_field.text.toString() != "") {
            from = from_field.text.toString()
        } else {
            Toast.makeText(
                context,
                "All the fields must be completed",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (to_field.text.toString() != "") {
            to = to_field.text.toString()
        } else {
            Toast.makeText(
                context,
                "All the fields must be completed",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (location_field.text.toString() != "") {
            location = location_field.text.toString()
        } else {
            Toast.makeText(
                context,
                "All the fields must be completed",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        vm.slot.observe(viewLifecycleOwner) {
            if (title != slot.title || description != slot.description || date != slot.date ||
                "$from-$to" != slot.duration || location != slot.location
            ) {

                var hours = (to.split(":")[0].toInt() - from.split(":")[0].toInt())*-1
                Log.i("Test hours", hours.toString())
                if (hours == 0) {
                    hours = 1
                }
                Log.i("Test hours", hours.toString())
                var new = Slot(
                    title,
                    description,
                    date,
                    "$from-$to",
                    location,
                    0,
                    id,
                    "Available",
                    hours
                )
                new.reference(slot.id)
                Log.i("test", new.toString())
                if (title != "" && description != "") {
                    vm.addSlot(new)
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.timeSlotDetailsFragment)
        return true
    }

}






