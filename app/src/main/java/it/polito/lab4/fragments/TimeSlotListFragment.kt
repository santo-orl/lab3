package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.timeSlots.Slot
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Adapter_frgTime
import it.polito.lab4.timeSlots.SlotUI

import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class TimeSlotListFragment: Fragment(R.layout.fragment_time_slot_list) {
    private lateinit var adapterFrgTime: Adapter_frgTime
    private var slotList: ArrayList<Slot> = arrayListOf()
    private lateinit var add_button: FloatingActionButton
    private val vm: ProfileViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var id : String
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

        add_button = view.findViewById(R.id.add_FAB)
        add_button.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack(TimeSlotEditFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimeSlotEditFragment>(R.id.myNavHostFragment)
            }
        }
        vm.email.observe(this.viewLifecycleOwner) {
            id = it
            readData(id)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.commit {
                    addToBackStack(HomeFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<HomeFragment>(R.id.myNavHostFragment)
                }

            }
        })
    }

    private fun readData(id: String) {
        db.collection("slots").document(id).get().addOnSuccessListener {
            slotList = arrayListOf()
            if (it.exists()) {
                it.data!!.forEach { (c, s) ->
                    //Log.i("testList", s.toString())
                    s as HashMap<*, *>
                    slotList.add(
                        Slot(
                            s["title"].toString(),
                            s["description"].toString(),
                            s["date"].toString(),
                            s["duration"].toString(),
                            s["location"].toString(),
                            slotList.size
                        )
                    )
                }
            }
            if (slotList.isEmpty()) {
                Log.i("testList", slotList.toString())
                slotList.add(
                    Slot(
                        "No advertisement",
                        "Click on the button below to add your first advertisement",
                        "",
                        "",
                        "",
                        0
                    )
                )
            }
            Log.i("testList2", slotList.toString())
            recycler_view.layoutManager = LinearLayoutManager(requireView().context)
            adapterFrgTime = Adapter_frgTime(slotList)
            recycler_view.adapter = adapterFrgTime
            vm.setSlot("")

            adapterFrgTime.setOnTodoDeleteClick(object : SlotUI.SlotListener {
                override fun onSlotDeleted(position: Int) {
                    vm.deleteSlot(slotList[position].title)
                    slotList.removeAt(position)
                    adapterFrgTime.notifyDataSetChanged()
                    if(slotList.size==0){
                        slotList.add(
                            Slot(
                                "No advertisement",
                                "Click on the button below to add your first advertisement",
                                "",
                                "",
                                "",
                                0
                            )
                        )
                    }
                }

                override fun onSlotClick(position: Int) {
                    Log.i("test on click", slotList.toString())
                    vm.setSlot(slotList[position].title)
                }
            })
        }
    }

}