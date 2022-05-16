package it.polito.lab4.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.ProfileViewModel
import it.polito.lab4.R
import it.polito.lab4.User
import it.polito.lab4.skills.Adapter_showProfile
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Slot
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
    private lateinit var adapterShowProfile: Adapter_showProfile
    private val vm: ProfileViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""
    private lateinit var slot: Slot
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //per mostrare il menu

        //inflate
        return inflater.inflate(R.layout.fragment_show_profile, container, false)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.setTitle("Profile")

        name_field = view.findViewById(R.id.name)
        nickname_field = view.findViewById(R.id.nickname)
        email_field = view.findViewById(R.id.email)
        location_field = view.findViewById(R.id.location)
        photo_field = view.findViewById(R.id.imageView)

        vm.email.observe(this.viewLifecycleOwner) {
              id = it
        }
        vm.slot.observe(this.viewLifecycleOwner) {
            slot = it
            // Log.i("test_edit", slot.toString())
            if (slot.user!= ""){
               readData(slot.user)
            }else{
                readData(id)
                setHasOptionsMenu(true)
            }


        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@ShowProfileFragment.activity?.supportFragmentManager?.popBackStack()
                //findNavController().navigate(R.id.action_showProfileFragment_to_homeFragment)


            }
        })
        super.onViewCreated(view, savedInstanceState)
        }

    private fun readData(id: String) {
        db.collection("users").document(id).get().addOnSuccessListener {
            if (it.get("name").toString() != "Full name") {
                name_field.text = it.get("name").toString()
            }
            email_field.text = it.get("email").toString()

            if (it.get("photoString").toString() != "") {
                uriImageString = it.get("photoString").toString()
                val uriImage = Uri.parse(uriImageString)
                photo_field.setImageURI(uriImage)
            } else {
                photo_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
            }
            if (it.get("nickname").toString() != "Nickname") {
                nickname_field.text = it.get("nickname").toString()
            }
            if (it.get("location").toString() != "Location") {
                location_field.text = it.get("location").toString()
            }

        }.addOnFailureListener { e ->
            Log.i("test_show", "Error adding document", e)
        }

        db.collection("skills").document(id).get().addOnSuccessListener {
            skillList = arrayListOf()
            if (it.exists()) {
               it.data!!.forEach { (c,s) ->
                   s as HashMap<*,*>
                   skillList.add(Skill(s["title"].toString(),s["description"].toString(),s["pos"].toString().toInt()))
                }
                recycler.layoutManager = LinearLayoutManager(this.activity)
                adapterShowProfile = Adapter_showProfile(skillList)
                recycler.adapter = adapterShowProfile

            }
        }.addOnFailureListener { e ->
            Log.i("test_show", "Error adding document", e)
        }
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
            adapterShowProfile = Adapter_showProfile(skillList)
            recycler.adapter = adapterShowProfile
        }

        super.onViewStateRestored(savedInstanceState)
    }


}
