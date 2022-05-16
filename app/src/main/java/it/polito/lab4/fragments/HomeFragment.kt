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
        var menuItem = menu.findItem(R.id.search_view)
        var searchView =  MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchData(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }

        })
    }

    private fun searchData(s: String?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Home - list of advertisement")

    /*    search_view = view.findViewById(R.id.search_view)

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.i("QUERY1", search_view.query.toString())
                searchSkills(search_view.query.toString(), id)
               // Toast.makeText(context, search_view.query, Toast.LENGTH_SHORT).show()

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                    this.onQueryTextSubmit("")

                //searchSkills(search_view.query.toString(), id)
                //Toast.makeText(context,search_view.query,Toast.LENGTH_SHORT).show()
                return true
            }


        })*/

        /*val welcome_text = view.findViewById<TextView>(R.id.welcome_text)
            welcome_text.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in));*/
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
                            Log.i("test_home", s.toString())
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
                        vm.setdesc(skillList[position].description)


                    }

                    override fun onSkillDeleted(position: Int) {

                    }

                })

            }
            .addOnFailureListener { exception ->
                Log.i("test_home", "Error getting documents: ", exception)
            }
        /*
        adapterSkill.setOnTodoDeleteClick(object : SkillUI.SkillListener {
            override fun onSlotDeleted(position: Int) {
                var query =
                    db.collection("skills").whereEqualTo("title", skillList[position].title)
                Log.i("test_slot", query.toString())
                /*query.delete()
                .addOnSuccessListener { Log.d("test_slot", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("test_slot", "Error deleting document", e) }*/
                skillList.removeAt(position)
                adapterSkill.notifyDataSetChanged()
            }

            override fun onSkillClick(position: Int) {
                Log.i("test on click", skillList.toString())
                vm.setSlot(skillList[position].title)
            }
        })
    }*/


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }


    private fun searchSkills(query: String, id: String) {
        skillList = arrayListOf()
        Log.i("QUERY", query)
        if(query!="") {
            db.collection("skills").whereEqualTo("$query" + ".title", query).get()
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
                }
                .addOnFailureListener { exception ->
                    Log.i("test_SKILLS", "Error getting SKILLS: ", exception)
                }
        }
        if(query=="") {
            //prende tutte le skills
            db.collection("skills")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id != id) {
                            document.data.forEach { (c, s) ->
                                Log.i("test_home", s.toString())
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
                                "No advertisements yet!",
                                "No one has posted a skill, be the first to add one!",
                                0
                            )
                        )
                    }
                }
        }


            Log.i("testList2", skillList.toString())
            recycler_view.layoutManager = LinearLayoutManager(this.activity)
            adapterSkill = Adapter_homeFrg(skillList)
            recycler_view.adapter = adapterSkill

            adapterSkill.setOnTodoClick(object : SkillUI.SkillListener {
                override fun onSkillClick(position: Int) {
                    vm.setSkill(skillList[position].title)
                    vm.setdesc(skillList[position].description)
                }

                override fun onSkillDeleted(position: Int) {
                }

            })

        }


}





