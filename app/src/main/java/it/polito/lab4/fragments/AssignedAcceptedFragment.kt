package it.polito.lab4.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.skills.Adapter_SkillUserFrg
import it.polito.lab4.skills.Skill


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



}