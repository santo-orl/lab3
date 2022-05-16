package it.polito.lab4.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.R
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import it.polito.lab4.timeSlots.Adapter_homeFrg
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class HomeFragment : Fragment(R.layout.fragment_home_skilllist) {
    private lateinit var adapterSkill: Adapter_homeFrg
    private var skillList: ArrayList<Skill> = arrayListOf()
    private val vm: ProfileViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""
    private lateinit var search_view: SearchView


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        Log.i("test","menu")
        var menuItem = view?.findViewById<SearchView>(R.id.action_search)
        menuItem?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchSkills(p0?.lowercase(),id)
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Home")

        Log.i("test_home2", id)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    var a = Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a)

                }
            })


    }


    private fun readData(id: String) {
        skillList = arrayListOf()
        db.collection("skills")
            .get()
            .addOnSuccessListener {
                    result ->
                for (document in result) {
                    if (document.id != id) {
                        document.data.forEach { (c, s) ->
                            Log.i("test_home!!!!", s.toString())
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
                }
                if (skillList.isEmpty()) {
                    Log.i("testList", skillList.toString())
                    skillList.add(
                        Skill(
                            "No skills available",
                            "Other users have not yet placed advertisements, be the first, add it in your profile!",
                            0
                        )
                    )
                }
                Log.i("testList2", skillList.toString())
                recycler_view.layoutManager = LinearLayoutManager(this.activity)
                adapterSkill = Adapter_homeFrg(skillList)
                recycler_view.adapter = adapterSkill

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


    private fun searchSkills(query: String?, id: String) {
        skillList = arrayListOf()
        Log.i("QUERY", query.toString())
            db.collection("skills").whereEqualTo("search", query).get()
                .addOnSuccessListener {
                    //db.collection("skills").orderBy(query).get().addOnSuccessListener {
                        result ->
                    for (document in result) {
                        Log.i("TEST_DOCUMENT", "$document")

                        if (document.id != id) {
                            document.data.forEach { (c, s) ->
                                Log.i("test_home", s.toString())
                                s as HashMap<*, *>
                                if (s["title"] == query) {
                                    Log.i("test_home", "entra")
                                    skillList.add(
                                        Skill(
                                            s["title"].toString(),
                                            s["description"].toString(),
                                            s["pos"].toString().toInt()
                                        )
                                    )
                                }
                            }
                        }
                    }
                    if (skillList.isEmpty()) {
                        Log.i("testList", skillList.toString())
                        skillList.add(
                            Skill(
                                "No skills found",
                                "No one has the skill you are searching for!",
                                0
                            )
                        )
                    }
                    Log.i("testList2", skillList.toString())
                    recycler_view.layoutManager = LinearLayoutManager(this.activity)
                    adapterSkill = Adapter_homeFrg(skillList)
                    recycler_view.adapter = adapterSkill

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
                    Log.i("test_SKILLS", "Error getting SKILLS: ", exception)
                }



        }


}





