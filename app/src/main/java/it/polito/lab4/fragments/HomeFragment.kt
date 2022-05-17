package it.polito.lab4.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import it.polito.lab4.timeSlots.Adapter_homeFrg
import kotlinx.android.synthetic.main.fragment_show_profile.*
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class HomeFragment : Fragment(R.layout.fragment_home_skilllist) {
    private lateinit var adapterSkill: Adapter_homeFrg
    private var skillList: ArrayList<Skill> = arrayListOf()
    private val vm: ViewModel by activityViewModels()
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

        val menuItem = menu.findItem(R.id.action_search)
        val p = menuItem.actionView as SearchView
        Log.i("test", menuItem.toString())
        p.isIconifiedByDefault = false
        p.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(p0.toString().isNotEmpty()) {
                    Log.i("Test on text submit", p0.toString())
                    searchSkill(p0?.lowercase(), id)
                }
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0.toString().isEmpty()) {
                    Log.i("TEST", "mSearchView on close ")
                    readData(id)
                }
                return false
            }

        })
        p.setOnCloseListener {
            Log.i("TEST", "mSearchView on close ")
            readData(id)
            false
        }

    }

    private fun searchSkill(query: String?, id: String) {
        Log.i("test_home!!!!", "query $query")
        skillList = arrayListOf()
        db.collection("skills").whereNotEqualTo("user", id).whereEqualTo("search", query)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // if (document.id != id) {
                    val s = document.data as HashMap<*, *>
                    Log.i("test_home!!!!", s.toString())
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
                            "No skills found",
                            "No one has the skill you are searching for!",
                            0,
                            ""
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Home"

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


    fun readData(id: String) {
        skillList = arrayListOf()
        db.collection("skills").whereNotEqualTo("user", id)
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

                //}
                if (skillList.isEmpty()) {
                    Log.i("testList", skillList.toString())
                    skillList.add(
                        Skill(
                            "No skills available",
                            "Other users have not yet placed advertisements, be the first, add it in your profile!",
                            0,
                            ""
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

        //value=ObjectValue{internalValue={Leggere:{description:so leggere da 10 anni,pos:1,search:leggere,title:Leggere}}
        db.collection("skills").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.i("Test_DOCUMENT", "$document")
                    Log.i("Test_document_data", "${document.data}")
                    if (document.id != id) {
                        document.data.forEach { (c, s) ->
                            Log.i("test_home", s.toString())
                            s as HashMap<*, *>
                            if (s["search"].toString().contains(query as CharSequence)) {
                                Log.i("test_home", "entra")
                                skillList.add(
                                    Skill(
                                        s["title"].toString(),
                                        s["description"].toString(),
                                        s["pos"].toString().toInt(),
                                        s["user"].toString()
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
                            0,
                            ""
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









