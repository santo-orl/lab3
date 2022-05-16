package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.R
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import it.polito.lab4.skills.Adapter_SkillUserFrg
import kotlinx.android.synthetic.main.fragment_home_skilllist.view.*
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class ListSkillUserFragment : Fragment(R.layout.fragment_home_skilllist) {
    private lateinit var search_view: SearchView

    private lateinit var adapterSkill: Adapter_SkillUserFrg
    private var skillList: ArrayList<Skill> = arrayListOf()
    private val vm: ProfileViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home_skilllist, container, false)
        vm.email.observe(this.viewLifecycleOwner) {
            id = it
            if (id != "") {
                readData(id)
            }
            //
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("List your skills")
        search_view = view.findViewById(R.id.search_view)
        search_view.visibility = View.GONE

        /*val welcome_text = view.findViewById<TextView>(R.id.welcome_text)
        welcome_text.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in));*/
        Log.i("test_home2", id)



        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@ListSkillUserFragment.activity?.supportFragmentManager?.popBackStack()
                //findNavController().navigate(R.id.action_showProfileFragment_to_homeFragment)


            }
        })
    }

    private fun readData(id: String) {
        db.collection("skills").document(id).get().addOnSuccessListener {
            skillList = arrayListOf()
            if (it.exists()) {
                it.data!!.forEach { (c, s) ->
                    s as HashMap<*, *>
                    skillList.add(
                        Skill(
                            s["title"].toString(),
                            s["description"].toString(),
                            s["pos"].toString().toInt()
                        )
                    )
                }
            }
            if (skillList.isEmpty()) {
                Log.i("testList", skillList.toString())
                skillList.add(
                    Skill(
                        "No skills available!",
                        "Add new skills in your profile!",
                        0
                    )
                )
            }
            Log.i("testList2", skillList.toString())
            recycler_view.layoutManager = LinearLayoutManager(this.activity)
            adapterSkill = Adapter_SkillUserFrg(skillList)
            recycler_view.adapter = adapterSkill
            vm.setSkill("")

            adapterSkill.setOnTodoClick(object : SkillUI.SkillListener {
                override fun onSkillClick(position: Int) {
                    vm.setSkill(skillList[position].title)
                }

                override fun onSkillDeleted(position: Int) {

                }

            })

        }
            .addOnFailureListener { exception ->
                Log.i("test_home", "Error getting documents: ", exception)
            }
    }
}