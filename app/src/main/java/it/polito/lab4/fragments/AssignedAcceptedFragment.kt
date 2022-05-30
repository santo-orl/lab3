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
import it.polito.lab4.skills.Adapter_SkillUserFrg
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class AssignedAcceptedFragment : Fragment(R.layout.fragment_assigned_or_acceptedslot) {
    private lateinit var adapterSkill: Adapter_SkillUserFrg
    private var skillList: ArrayList<Skill> = arrayListOf()
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assigned_or_acceptedslot, container, false)
    }

    private fun readData(id: String) {
        skillList = arrayListOf()
        db.collection("assigned_accepted_slot")
            .whereEqualTo("accepterUser", id)
            .whereEqualTo("assignedUser", id)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // if (document.id != id) {
                    val s = document.data as HashMap<*, *>
                    Log.i("test_home!!!!", s.toString())
                    s as HashMap<*, *>
                    var p = Skill(
                        s["title"].toString(),
                        "",
                        0,
                        "",
                        ""
                    )
                  //  p.reference(s["id"].toString())
                    skillList.add(p)
                    Log.i("testList", p.toString())
                }

                }
                Log.i("testList2", skillList.toString())
                recycler_view.layoutManager = LinearLayoutManager(this.activity)
                adapterSkill = Adapter_SkillUserFrg(skillList)
                recycler_view.adapter = adapterSkill
                vm.setSkill("")
                vm.setDesc("")



            }



}