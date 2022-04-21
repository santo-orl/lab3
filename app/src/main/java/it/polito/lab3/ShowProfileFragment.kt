package it.polito.lab3

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_edit_profile.*


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {
   /*
    //to make the persistency unique
    private val sharedPrefFIle = "it.polito.showprofileactivityy"
*/
    //fields
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
    //skills field
    private lateinit var skillAdapter: Skill_Adapter

    //per differenziare i due recycler quando mostra le skills
    private lateinit var adapterText: Adapter_Text

    //field used to make the changes persistent
    lateinit var sharedPref: SharedPreferences;




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*if (savedInstanceState != null) {

            //persistency
           // sharedPref = this.getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)

            name = savedInstanceState.getString("Full name", "0")
            name_field.text = name
            nickname = savedInstanceState.getString("Nickname", "0")
            nickname_field.text = nickname
            location = savedInstanceState.getString("Location", "0")
            location_field.text = location
            email = savedInstanceState.getString("Email", "0")
            email_field.text = email

            if (savedInstanceState.getString("Picture", "0") != "") {
                uriImageString = savedInstanceState.getString("Picture", "0")
                uriImage = Uri.parse(uriImageString)
                photo_field.setImageURI(uriImage)
            }
            skillList = arrayListOf()
            skillList = savedInstanceState.getParcelableArrayList<Skill>("Skills") as ArrayList<Skill>

            recycler.layoutManager = LinearLayoutManager(context)
            adapterText = Adapter_Text(skillList)
            recycler.adapter = adapterText
            }
*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_profile, container, false)
    }
/*
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Full name", name)
        outState.putString("Nickname", nickname)
        outState.putString("Email", email)
        outState.putString("Location", location)
        outState.putString("Picture", uriImageString)
        outState.putParcelableArrayList("Skills", skillList)
        super.onSaveInstanceState(outState)



        //  outState.putParcelable("Skills", recycler.layoutManager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
        //outState.putString("Skills", skills)
    }

*/
/*
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }*/
}