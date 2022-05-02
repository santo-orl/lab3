package it.polito.lab3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.lab3.R
import it.polito.lab3.skills.Adapter_Text
import it.polito.lab3.skills.Skill
import kotlinx.android.synthetic.main.activity_edit_profile.*


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {

    var name: String = "Full name"
    var nickname: String = "Nickname"
    var location: String = "Location"
    var email: String = "email@address"
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


    private val profViewModel by activityViewModels<ProfileViewModel>()

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
        nickname_field = view.findViewById(R.id.nickname)
        email_field = view.findViewById(R.id.email)
        location_field = view.findViewById(R.id.location)
        photo_field = view.findViewById(R.id.imageView)

        profViewModel.name.observe(this.viewLifecycleOwner) {
            if (it != "") {
                name_field.text = it
            }else{
                name_field.text = name
            }
        }
        profViewModel.nickname.observe(this.viewLifecycleOwner) {
            if (it != "") {
                nickname_field.text = it
            }else{
                nickname_field.text = nickname
            }
        }
        profViewModel.email.observe(this.viewLifecycleOwner) {
            if (it != "") {
                email_field.text = it
            }else{
                email_field.text = email
            }
        }

        profViewModel.location.observe(this.viewLifecycleOwner) {
            if (it != "") {
                location_field.text = it
            }else{
                location_field.text = location
            }
        }

        profViewModel.photoString.observe(this.viewLifecycleOwner) {
            if (it != "") {
                uriImage = Uri.parse(it)
                photo_field.setImageURI(uriImage)
            }else{
                photo_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
            }
        }
         profViewModel.skills.observe(this.viewLifecycleOwner){
            if (it.isNotEmpty()){
                skillList= it
                recycler.layoutManager = LinearLayoutManager(this.activity)
                adapterText = Adapter_Text(skillList)
                recycler.adapter = adapterText
            }
        }

        sharedPref =
            this.requireActivity().getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)

        }

        //creo la pencil icon in alto a dx
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.main_menu, menu)
        }

        //gestisco il click
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId.equals(R.id.pencil)) {
                findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment)
                return true
            }
            return false
        }

}
