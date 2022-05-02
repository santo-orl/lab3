package it.polito.lab3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.*

import android.widget.ImageView

import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import it.polito.lab3.R
import it.polito.lab3.skills.Adapter_Text
import it.polito.lab3.skills.Skill


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {

    var name: String = "Full name"
    var nickname: String = "Nickname"
    var location: String = "Location"
    private var email: String = "email@address"
    private var skillList: ArrayList<Skill> = arrayListOf()
    private lateinit var uriImage: Uri
    private var uriImageString: String = ""
    //associated fields to layout
    lateinit var name_field: TextView
    lateinit var nickname_field: TextView
    lateinit var location_field: TextView
    lateinit var email_field: TextView
    lateinit var photo_field: ImageView
    private lateinit var state: Parcelable
    //per differenziare i due recycler quando mostra le skills
    private lateinit var adapterText: Adapter_Text

    private val sharedPrefFIle = "it.polito.showprofileactivityy"
    lateinit var sharedPref: SharedPreferences;

    lateinit var name_field: TextView

    private val profViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //per mostrare il menu
        setHasOptionsMenu(true)
        //inflate
        return inflater.inflate(R.layout.fragment_show_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name_field = view.findViewById(R.id.name)
        profViewModel.name.observe(this.viewLifecycleOwner){
            name_field.text = it
        }



        sharedPref = this.requireActivity().getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)


    }

    //creo la pencil icon in alto a dx
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    //gestisco il click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId.equals(R.id.pencil)){
            findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment)
            return true
        }
        return false
    }



}