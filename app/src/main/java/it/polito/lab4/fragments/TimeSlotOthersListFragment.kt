package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ViewModel
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Adapter_frgTime
import it.polito.lab4.timeSlots.Slot
import it.polito.lab4.timeSlots.SlotUI
import kotlinx.android.synthetic.main.fragment_time_slot_list.*

//Lista degli slot degli ALTRI UTENTI
class TimeSlotOthersListFragment : Fragment() {
    private lateinit var adapterFrgTime: Adapter_frgTime
    private var slotList: ArrayList<Slot> = arrayListOf()
    private lateinit var add_button: FloatingActionButton
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private  var id = ""
    private  var title = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_skilllist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("TimeSlotOthersList")

        /*add_button = view.findViewById(R.id.add_FAB)

        add_button.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack(TimeSlotEditFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimeSlotEditFragment>(R.id.myNavHostFragment)
            }
        }*/

        vm.email.observe(this.viewLifecycleOwner) {
            id = it.toString()

        }
        vm.skill.observe(this.viewLifecycleOwner) {
            title = it.toString()
            readData(id,title)

        }
        Log.i("title!!!!!!", title)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@TimeSlotOthersListFragment.activity?.supportFragmentManager?.popBackStack()
                //findNavController().navigate(R.id.action_showProfileFragment_to_homeFragment)


            }
        })
  /*      val timeSlot = view.findViewById<TextView>(R.id.editTitle)

        timeSlot.setOnClickListener{
            findNavController().navigate(R.id.action_itemListFragment_to_timeSlotDetailsFragment)
        }*/
    }

    private fun readData(id: String, title : String) {
        db.collection("slots")
            .whereEqualTo("title", title).get().addOnSuccessListener { result ->
                Log.i("TEST", "boh")
                slotList = arrayListOf()
                for (document in result) {

                        val s = document.data as HashMap<*, *>
                        if (s["user"] != id) {
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
                            0,
                            ""
                        )
                    )

                }
                Log.i("testList2", slotList.toString())

                recycler_view.layoutManager = LinearLayoutManager(requireView().context)
                adapterFrgTime = Adapter_frgTime(slotList)
                recycler_view.adapter = adapterFrgTime
                vm.setSlot(Slot("","","","","",-1,""))

                adapterFrgTime.setOnTodoDeleteClick(object : SlotUI.SlotListener {
                    override fun onSlotDeleted(position: Int) {
                        vm.setSlot(slotList[position])
                        vm.deleteSlot()
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
                                    0,
                                    ""
                                )
                            )
                        }
                    }

                    override fun onSlotClick(position: Int) {
                        Log.i("test on click", slotList.toString())
                        vm.setSlot(slotList[position])
                    }
                })
            }
    }

    
}
