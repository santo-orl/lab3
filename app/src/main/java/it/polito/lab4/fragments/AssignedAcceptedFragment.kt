package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.timeSlots.Adapter_OthersList
import it.polito.lab4.timeSlots.Slot
import it.polito.lab4.timeSlots.SlotUI
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class AssignedAcceptedFragment : Fragment() {

    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""

    private lateinit var adapterList: Adapter_OthersList
    private var slotList: ArrayList<Slot> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assigned_or_acceptedslot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Assigned / Accepted Slots")


        vm.email.observe(this.viewLifecycleOwner) {
            slotList = arrayListOf()
            id = it.toString()
            readDataAccepter(id)
            readDataAssigned(id)

        }


    }


    private fun readDataAccepter(id: String) {
        db.collection("assigned_accepted_slot")
            .whereEqualTo("accepterUser", id).get().addOnSuccessListener { result ->
                //.whereEqualTo("title", title).whereNotEqualTo("user", id).get().addOnSuccessListener { result ->
                //   Log.i("TEST", "boh")
                //slotList = arrayListOf()
                for (document in result) {
                    val s = document.data as HashMap<*, *>
                        //   Log.i("TEST", "${document.id} + ${document.data}  ")

                    //title = title, description = accepter user (OWNER), user = assigned user
                    var add = Slot(
                        s["title"].toString(),
                        s["accepterUser"].toString(),
                        "",
                        "",
                        "",
                        slotList.size,
                        s["assignedUser"].toString(),
                        "Sold",
                        0.0
                    )
                    add.id = s["id"].toString()
                        slotList.add(
                            add
                        )

                }
                if (slotList.isEmpty()) {
                    Log.i("testList", slotList.toString())
                    slotList.add(
                        Slot(
                            "No jobs yet",
                            "Hire someone or do a job!",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            -0.1
                        )
                    )

                }
                Log.i("testList2", slotList.toString())

                recycler_view.layoutManager = LinearLayoutManager(requireView().context)
                adapterList = Adapter_OthersList(slotList)
                recycler_view.adapter = adapterList
                vm.setSlot(Slot("", "", "", "", "", -1, "", "", -0.1))
                adapterList.setOnTodoDeleteClick(object : SlotUI.SlotListener {
                    override fun onSlotDeleted(position: Int) {
                    }

                    override fun onSlotClick(position: Int) {
                        Log.i("test on click", slotList.toString())
                        db.collection("slots").document(slotList[position].id).get()
                            .addOnSuccessListener { s ->
                                var add = Slot(
                                    s["title"].toString(),
                                    s["description"].toString(),
                                    s["date"].toString(),
                                    s["duration"].toString(),
                                    s["location"].toString(),
                                    slotList.size,
                                    s["user"].toString(),
                                    s["status"].toString(),
                                    s["hours"].toString().toDouble()
                                )
                                add.id = s["id"].toString()
                                slotList.add(
                                    add
                                )

                                vm.setSlot(add)
                            }
                    }
                })
            }
    }


    private fun readDataAssigned(id:String){
        db.collection("assigned_accepted_slot")
            .whereEqualTo("assignedUser", id).get().addOnSuccessListener { result ->
                //.whereEqualTo("title", title).whereNotEqualTo("user", id).get().addOnSuccessListener { result ->
                //   Log.i("TEST", "boh")
                //slotList = arrayListOf()
                for (document in result) {
                    val s = document.data as HashMap<*, *>
                    //   Log.i("TEST", "${document.id} + ${document.data}  ")
                    //title = title, description = accepter user (OWNER), user = assigned user
                    var add = Slot(
                        s["title"].toString(),
                        s["accepterUser"].toString(),
                        "",
                        "",
                        "",
                        slotList.size,
                        s["assignedUser"].toString(),
                        "Sold",
                        0.0
                    )
                    add.id = s["id"].toString()
                    slotList.add(
                        add
                    )

                }
                if (slotList.isEmpty()) {
                    Log.i("testList", slotList.toString())
                    slotList.add(
                        Slot(
                            "No advertisement",
                            "No advertisements for this skill",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            -0.1
                        )
                    )

                }
                Log.i("testList2", slotList.toString())

                recycler_view.layoutManager = LinearLayoutManager(requireView().context)
                adapterList = Adapter_OthersList(slotList)
                recycler_view.adapter = adapterList
                vm.setSlot(Slot("", "", "", "", "", -1, "", "", -0.1))
                adapterList.setOnTodoDeleteClick(object : SlotUI.SlotListener {
                    override fun onSlotDeleted(position: Int) {
                    }

                    override fun onSlotClick(position: Int) {
                         Log.i("test on click", slotList.toString())
                        db.collection("slots").document(slotList[position].id).get()
                            .addOnSuccessListener { s ->
                                var add = Slot(
                                    s["title"].toString(),
                                    s["description"].toString(),
                                    s["date"].toString(),
                                    s["duration"].toString(),
                                    s["location"].toString(),
                                    slotList.size,
                                    s["user"].toString(),
                                    s["status"].toString(),
                                    s["hours"].toString().toDouble()
                                )
                                add.id = s["id"].toString()
                                slotList.add(
                                    add
                                )
                                Log.i("test on click", add.toString())
                                vm.setSlot(add)
                            }

                    }
                })
            }
    }
}