package it.polito.lab4.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.skills.Adapter_showProfile
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Slot
import kotlinx.android.synthetic.main.fragment_show_profile.*
import java.io.File


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {

    private var skillList: ArrayList<Skill> = arrayListOf()
    private var uriImageString: String = ""

    //associated fields to layout
    private lateinit var name_field: TextView
    private lateinit var nickname_field: TextView
    private lateinit var location_field: TextView
    private lateinit var email_field: TextView
    private lateinit var photo_field: ImageView
    private lateinit var hour_field: TextView
    private lateinit var ratingBar : RatingBar

    //per differenziare i due recycler quando mostra le skills
    private lateinit var adapterShowProfile: Adapter_showProfile
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var storage = Firebase.storage("gs://timebankingapplication")
    var storageRef = storage.reference
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
    override fun onResume() {
        super.onResume()
        if (!this.isAdded) {
            Log.i("Test", "oooo")
            val trans: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            if (trans != null) {
                trans.replace(R.id.myNavHostFragment, ShowProfileFragment())
                trans.commit()
            }else{
                vm.setSlot(Slot("","","","","",-1,"",""))
            }
            //getSupportFragmentManager().executePendingTransactions();   //unnecessary
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.setTitle("Profile")

        name_field = view.findViewById(R.id.name)
        nickname_field = view.findViewById(R.id.nickname)
        email_field = view.findViewById(R.id.email)
        location_field = view.findViewById(R.id.location)
        photo_field = view.findViewById(R.id.imageView)
        hour_field = view.findViewById(R.id.hour)

        ratingBar = view.findViewById(R.id.simpleRatingBar)
        ratingBar.setIsIndicator(true)
        ratingBar.numStars = 5
        ratingBar.rating = 1F
        ratingBar.stepSize = 1F

        vm.email.observe(this.viewLifecycleOwner) {
              id = it
        }

        vm.slot.observe(this.viewLifecycleOwner) {
            slot = it
            Log.i("ALLORA", slot.toString())
            if (slot.user!= ""){
               readData(slot.user)
                hour_field.visibility = View.GONE
            }else{
                readData(id)
                Log.i("test_show", "id utente"+id)
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
            if(it.exists()) {
                if (it.get("name").toString() != "Full name") {
                    name_field.text = it.get("name").toString()
                }
                email_field.text = it.get("email").toString()

                if (it.get("photoString").toString() != "") {

                    val pathReference = storageRef.child("images/"+email_field.text.toString())
                    //Log.i("TEST", pathReference.toString())
                    uriImageString = pathReference.toString()
                    val localFile = File.createTempFile("images", "jpg")

                    pathReference.getFile(localFile).addOnSuccessListener {
                        // Local temp file has been created
                        val uriImage = Uri.parse(localFile.path)
                      //  Log.i("TEST", uriImage.toString())
                       // Log.i("test_show", localFile.path.toString())
                        photo_field.setImageURI(uriImage)
                    }.addOnFailureListener {
                        // Handle any errors
                    }

                  Log.i("test_show", pathReference.toString())
                }else {
                    photo_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
                }
                if (it.get("nickname").toString() != "null") {
                    nickname_field.text = it.get("nickname").toString()
                }else{
                    nickname_field.text = "Nickname"
                }
                if (it.get("location").toString() != "null") {
                    location_field.text = it.get("location").toString()
                }else{
                    location_field.text = "Location"
                }
                if(it.get("hours").toString() != "-1") {
                    hour_field.text = it.get("hours").toString()
                }else{
                    hour_field.text = "-1"
                }
            }else{
                /*vm.name.observe(this.viewLifecycleOwner) {
                    id = it
                }*/
            }
        }.addOnFailureListener { e ->
            Log.i("test_show", "Error adding document", e)
        }

        db.collection("skills").whereEqualTo("user", id).get().addOnSuccessListener { result ->
            skillList = arrayListOf()
           /* if (it.exists()) {
               it.data!!.forEach { (c,s) ->
                   s as HashMap<*,*>
                   skillList.add(Skill(s["title"].toString(),s["description"].toString(),s["pos"].toString().toInt(), id,s["title"].toString().lowercase()))
                }*/
                for (document in result) {
                    val s = document.data as HashMap<*, *>
                    Log.i("test_home!!!!", s.toString())
                    var p = Skill(
                        s["title"].toString(),
                        s["description"].toString(),
                        s["pos"].toString().toInt(),
                        s["user"].toString()
                    )
                    p.reference(s["id"].toString())
                    skillList.add(p)
                    }


                recycler.layoutManager = LinearLayoutManager(this.activity)
                adapterShowProfile = Adapter_showProfile(skillList)
                recycler.adapter = adapterShowProfile

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
  /*  override fun onSaveInstanceState(outState: Bundle) {
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
    }*/


}
