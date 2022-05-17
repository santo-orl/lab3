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
import it.polito.lab4.ViewModel
import it.polito.lab4.timeSlots.Slot
import it.polito.lab4.R
import it.polito.lab4.timeSlots.Adapter_UserList
import it.polito.lab4.timeSlots.SlotUI

import kotlinx.android.synthetic.main.fragment_time_slot_list.*

//Lista degli slot dell'UTENTE
class TimeSlotUserListFragment: Fragment(R.layout.fragment_time_slot_list) {
    private lateinit var adapterUserList: Adapter_UserList
    private var slotList: ArrayList<Slot> = arrayListOf()
    private lateinit var add_button: FloatingActionButton
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private  var id = ""
    private  var title = ""

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
        activity?.setTitle("Advertisement")

        add_button = view.findViewById(R.id.add_FAB)
        //add_button.visibility = View.GONE
        add_button.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack(TimeSlotEditFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimeSlotEditFragment>(R.id.myNavHostFragment)
            }
        }
        vm.email.observe(this.viewLifecycleOwner) {
            id = it.toString()

        }
        vm.skill.observe(this.viewLifecycleOwner) {
            title = it.toString()
            readData(id,title)

        }
        Log.i("title!!!!!!", title)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
              activity?.supportFragmentManager?.commit {
                    addToBackStack(ListSkillUserFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<ListSkillUserFragment>(R.id.myNavHostFragment)
                }

            }
        })
    }

    private fun readData(id: String, title : String) {
        db.collection("slots")
            .whereEqualTo("title", title).whereEqualTo("user", id).get().addOnSuccessListener {  result ->
                Log.i("TEST", "boh")
                slotList = arrayListOf()
                for (document in result) {
                    val s = document.data as HashMap<*, *>
                    Log.i("TEST", "${document.id} + ${document.data}  ")
                    var add = Slot(
                        s["title"].toString(),
                        s["description"].toString(),
                        s["date"].toString(),
                        s["duration"].toString(),
                        s["location"].toString(),
                        slotList.size,
                        s["user"].toString(),

                    )
                    add.id = s["id"].toString()
                    slotList.add(
                        add
                    )


                }
                Log.i("Slot List User", slotList.toString())
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
                adapterUserList = Adapter_UserList(slotList)
                recycler_view.adapter = adapterUserList
                vm.setSlot(Slot("","","","","",-1,""))

                adapterUserList.setOnTodoDeleteClick(object : SlotUI.SlotListener {
                override fun onSlotDeleted(position: Int) {
                    vm.deleteSlot(slotList[position])
                    slotList.removeAt(position)
                    adapterUserList.notifyDataSetChanged()
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


