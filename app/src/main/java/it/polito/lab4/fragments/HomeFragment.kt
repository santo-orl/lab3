package it.polito.lab4.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.R
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import it.polito.lab4.timeSlots.Adapter_editProfile
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class HomeFragment : Fragment(R.layout.fragment_home_skilllist) {
    private lateinit var adapterSkill: Adapter_editProfile
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
            if(id!=""){
                readData(id)
            }
          Log.i("test_home", id)
          //
      }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Home")

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
        db.collection("skills").document(id).get().addOnSuccessListener {
            skillList = arrayListOf()
            if (it.exists()) {
                it.data!!.forEach { (c, s) ->
                    //Log.i("testList", s.toString())
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
                        "No skills available",
                        "No one has published any skill",
                        0
                    )
                )
            }
            Log.i("testList2", skillList.toString())
            recycler_view.layoutManager = LinearLayoutManager(this.activity)
            adapterSkill = Adapter_editProfile(skillList)
            recycler_view.adapter = adapterSkill

        adapterSkill.setOnTodoClick(object : SkillUI.SkillListener {
                override fun onSkillClick(position: Int) {
                    vm.setSkill(skillList[position].title)
                }

                override fun onSkillDeleted(position: Int) {

                }

            })


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

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
}