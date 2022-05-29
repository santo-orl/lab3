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
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ViewModel
import it.polito.lab4.R
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import it.polito.lab4.skills.Adapter_SkillUserFrg
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class ListSkillUserFragment : Fragment(R.layout.fragment_home_skilllist) {
    private lateinit var search_view: SearchView

    private lateinit var adapterSkill: Adapter_SkillUserFrg
    private var skillList: ArrayList<Skill> = arrayListOf()
    private val vm: ViewModel by activityViewModels()
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
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("List of your time slot")
      /*  search_view = view.findViewById(R.id.search_view)
        search_view.visibility = View.GONE*/

        /*val welcome_text = view.findViewById<TextView>(R.id.welcome_text)
        welcome_text.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in));*/
        Log.i("test_home2", id)



        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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
        skillList = arrayListOf()
        db.collection("skills").whereEqualTo("user", id)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // if (document.id != id) {
                    val s = document.data as HashMap<*, *>
                    Log.i("test_home!!!!", s.toString())
                    s as HashMap<*, *>
                    var p = Skill(
                        s["title"].toString(),
                        s["description"].toString(),
                        s["pos"].toString().toInt(),
                        s["user"].toString(),
                        s["search"].toString(),
                    )
                    p.reference(s["id"].toString())
                    skillList.add(p)
                    Log.i("testList", p.toString())
                }

                if (skillList.isEmpty()) {
                Log.i("testList", skillList.toString())
                skillList.add(
                    Skill(
                        "No skills available!",
                        "Add new skills in your profile!",
                        0, ""
                    )
                )
            }
            Log.i("testList2", skillList.toString())
            recycler_view.layoutManager = LinearLayoutManager(this.activity)
            adapterSkill = Adapter_SkillUserFrg(skillList)
            recycler_view.adapter = adapterSkill
            vm.setSkill("")
            vm.setDesc("")

            adapterSkill.setOnTodoClick(object : SkillUI.SkillListener {
                override fun onSkillClick(position: Int) {
                    vm.setSkill(skillList[position].title)
                    vm.setDesc(skillList[position].description)
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