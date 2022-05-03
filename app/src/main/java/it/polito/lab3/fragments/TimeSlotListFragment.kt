package it.polito.lab3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.skills.SkillUI
import it.polito.lab3.timeSlots.Adapter_frgTime
import it.polito.lab3.timeSlots.Slot
import it.polito.lab3.timeSlots.SlotUI
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class TimeSlotListFragment: Fragment(R.layout.fragment_time_slot_list) {
    private lateinit var adapterFrgTime: Adapter_frgTime
    private var slotList: ArrayList<Slot> = arrayListOf()
    private lateinit var add_button: FloatingActionButton
    private val timeSlotViewModel: TimeSlotViewModel by activityViewModels()

    private val sharedPrefFIle = "it.polito.lab3.timeSlott"
    lateinit var sharedPref: SharedPreferences

 override fun onCreateView(
     inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
): View {
    // Inflate the layout for this fragment
    val view: View = inflater.inflate(R.layout.fragment_time_slot_list, container, false)
    // Add the following lines to create RecyclerView
    return view
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("List of advertisements")
        sharedPref = this.requireActivity().getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)

        add_button = view.findViewById(R.id.add_FAB)
        add_button.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack(TimeSlotEditFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimeSlotEditFragment>(R.id.myNavHostFragment)
            }
        }

        timeSlotViewModel.slots.observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                slotList = it
                Log.i("test", slotList[0].toString())
            }
            else{
                var slots = sharedPref.getString("id_slots","")
                Log.i("elenco",slots.toString())
                if(slots != ""){
                    var ll = slots!!.split("&&&")
                    for(s in ll){
                        var slotItem = s.split("###")
                        //ho dovuto mettere "" perchè salva solo i primi due campi
                        //altrimenti quando recupera i dati va in IndexOutOfBoundsException
                        slotList.add(Slot(slotItem[0],slotItem[1],slotItem[2],slotItem[3],slotItem[4]))
                    }
                    timeSlotViewModel.setSlots(slotList)
                }
            }
            if (slotList.isEmpty()) {
                val prova = ArrayList<Slot>(arrayListOf())
                prova.add(
                    Slot(
                        "No advertisement",
                        "Click on the button below to add your first advertisement",
                        "",
                        "",
                        ""
                    )
                )
                recycler_view.layoutManager = LinearLayoutManager(view.context)
                adapterFrgTime = Adapter_frgTime(prova)
                recycler_view.adapter = adapterFrgTime
            } else {
                recycler_view.layoutManager = LinearLayoutManager(view.context)
                adapterFrgTime = Adapter_frgTime(slotList)
                    recycler_view.adapter = adapterFrgTime
                }
            adapterFrgTime.setOnTodoDeleteClick(object : SlotUI.SlotListener {
                override fun onSlotDeleted(position: Int) {
                    slotList.removeAt(position)
                    adapterFrgTime.notifyDataSetChanged()
                }

            })

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val editor : SharedPreferences.Editor = sharedPref.edit()
                Log.i("prova2",slotList.toString())
                var ss = slotList.joinToString("&&&")
                editor.putString("id_slots",ss)
                editor.apply()
            }
        })
    }
}



