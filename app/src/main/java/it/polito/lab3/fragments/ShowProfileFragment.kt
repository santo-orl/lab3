package it.polito.lab3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.lab3.ProfileViewModel
import it.polito.lab3.R
import it.polito.lab3.skills.Adapter_Text
import it.polito.lab3.skills.Skill
import kotlinx.android.synthetic.main.activity_edit_profile.*


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {

    private var skillList: ArrayList<Skill> = arrayListOf()
    private lateinit var uriImage: Uri
    private var uriImageString: String = ""

    //associated fields to layout
    private lateinit var name_field: TextView
    private lateinit var nickname_field: TextView
    private lateinit var location_field: TextView
    private lateinit var email_field: TextView
    private lateinit var photo_field: ImageView

    //per differenziare i due recycler quando mostra le skills
    private lateinit var adapterText: Adapter_Text

    private val sharedPrefFIle = "it.polito.lab3"
    lateinit var sharedPref: SharedPreferences;

    private val profViewModel by activityViewModels<ProfileViewModel>()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

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

        activity?.setTitle("Profile")
        sharedPref =
            this.requireActivity().getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)

        name_field = view.findViewById(R.id.name)
        nickname_field = view.findViewById(R.id.nickname)
        email_field = view.findViewById(R.id.email)
        location_field = view.findViewById(R.id.location)
        photo_field = view.findViewById(R.id.imageView)


        //Log.i("test1", sharedPref.getString("id_name","hoooooooooo").toString())
        profViewModel.name.observe(this.viewLifecycleOwner) {
            if (it != "") {
                Log.i("test_nome", it.toString())
                name_field.text = it
            }else{
                //Log.i("test2", "if")
                name_field.text = sharedPref.getString("id_name","Full name").toString()
                profViewModel.setName(name_field.text.toString())
            }
        }
        profViewModel.nickname.observe(this.viewLifecycleOwner) {
            if (it != "") {
                nickname_field.text = it
            }else{
                Log.i("test_nick", sharedPref.getString("id_nickname","Nickname").toString())
                nickname_field.text = sharedPref.getString("id_nickname","Nickname").toString()
                profViewModel.setNickname(nickname_field.text.toString())
            }
        }
        profViewModel.email.observe(this.viewLifecycleOwner) {
            if (it != "") {
                email_field.text = it
            }else{
                email_field.text =  sharedPref.getString("id_email","email@address").toString()
                profViewModel.setEmail(email_field.text.toString())
            }
        }

        profViewModel.location.observe(this.viewLifecycleOwner) {
            if (it != "") {
                location_field.text = it
            }else{
                location_field.text =  sharedPref.getString("id_location","Location").toString()
                profViewModel.setLocation(location_field.text.toString())
            }
        }

        profViewModel.photoString.observe(this.viewLifecycleOwner) {
            if (it != "") {
                uriImageString = it
                uriImage = Uri.parse(it)
       //         photo_field.setImageURI(uriImage)
            }else{
                uriImageString = sharedPref.getString("id_photo", "").toString()
                if(uriImageString!= "") {
                    uriImage = Uri.parse(uriImageString)
                 //   photo_field.setImageURI(uriImage)
                    profViewModel.setPhoto(uriImageString)
                }else{
                    photo_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
                }

            }
        }
        profViewModel.skills.observe(this.viewLifecycleOwner){
            Log.i("test1", "observe")
            Log.i("test1", it.toString())

            if (it.isNotEmpty()){
                skillList = it
                Log.i("tes1", "empty")
            }else{
                var skills = sharedPref.getString("id_skills","")
                Log.i("test1444", skills.toString())
                if(skills != "") {
                    var ll = skills!!.split("&&&")
                    for(s in ll){
                        var skillItem = s.split("###")
                        skillList.add(Skill(skillItem[0], skillItem[1],skillItem[2].toInt()))
                    }
                    profViewModel.setSkills(skillList)
            }
            }
            if(skillList.isNotEmpty()){
                Log.i("test10", skillList.toString())
                recycler.layoutManager = LinearLayoutManager(this.activity)
                adapterText = Adapter_Text(skillList)
                recycler.adapter = adapterText
            }


        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString("id_name", name_field.text.toString())
                    editor.putString("id_nickname", nickname_field.text.toString())
                    editor.putString("id_email", email_field.text.toString())
                    editor.putString("id_location", location_field.text.toString())
                Log.i("test488", uriImageString)
                    editor.putString("id_photo", uriImageString)
                    var ss = skillList.joinToString("&&&")
                    editor.putString("id_skills",ss)

                editor.apply()
                this@ShowProfileFragment.activity?.supportFragmentManager?.popBackStack()
                //findNavController().navigate(R.id.action_showProfileFragment_to_homeFragment)


            }
        })
        super.onViewCreated(view, savedInstanceState)
        }


        //creo la pencil icon in alto a dx
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.main_menu, menu)
        }

        //gestisco il click
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId.equals(R.id.pencil)) {
                activity?.supportFragmentManager?.commit {
                    addToBackStack(EditProfileFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<EditProfileFragment>(R.id.myNavHostFragment)
                }
               // findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment)
                return true
            }
            return false
        }

    /* function used for saving fields' state */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Full name", "nome")
        outState.putString("Nickname", "nick")
        outState.putString("Email","mail")
        outState.putString("Location", "location")
        outState.putString("Picture", uriImageString)
        outState.putParcelableArrayList("Skills", skillList)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("Full name", "0")
            name_field.text = name
            val nickname = savedInstanceState.getString("Nickname", "0")
            nickname_field.text = nickname
            val location = savedInstanceState.getString("Location", "0")
            location_field.text = location
            val email = savedInstanceState.getString("Email", "0")
            email_field.text = email

            if (savedInstanceState.getString("Picture", "0") != "") {
                uriImageString = savedInstanceState.getString("Picture", "0")
                uriImage = Uri.parse(uriImageString)
                photo_field.setImageURI(uriImage)
            }
            skillList = arrayListOf()
            skillList = savedInstanceState.getParcelableArrayList<Skill>("Skills") as ArrayList<Skill>

            recycler.layoutManager = LinearLayoutManager(this.activity)
            adapterText = Adapter_Text(skillList)
            recycler.adapter = adapterText
        }

        super.onViewStateRestored(savedInstanceState)
    }


}
