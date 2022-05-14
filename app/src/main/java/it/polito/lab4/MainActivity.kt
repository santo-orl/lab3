package it.polito.lab4

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

import it.polito.lab4.fragments.ShowProfileFragment
import it.polito.lab4.fragments.TimeSlotListFragment


// Main activity, is the base for all the fragments
class MainActivity : AppCompatActivity(){


    //per gestire il menu che si apre a sinistra
    lateinit var nv : NavigationView
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    private val profViewModel by viewModels<ProfileViewModel>()
    private lateinit var name_field: TextView
    private lateinit var email_field: TextView
    private lateinit var image_field: ImageView
    private val sharedPrefFIle = "it.polito.lab3"
    lateinit var sharedPref: SharedPreferences;
    private  var name= "Your name"
    private  var email= "Your email"
    private  var uriImageString: String = ""
    private  var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("TEST", "MAIN ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportFragmentManager.commit {
            addToBackStack(HomeFragment::class.toString())
            setReorderingAllowed(true)
            replace(R.id.myNavHostFragment, HomeFragment())
        }

        sharedPref =
            this.getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)
        //per gestire la visibilità del menu a sinistra e agganciare i listener
        drawerLayout = findViewById(R.id.my_drawer_layout)
        nv = findViewById(R.id.nav_view)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //collego i listener al menu a sinistra che si apre
        nv.setNavigationItemSelectedListener {
            when (it.itemId) {
                //mostro l'home fragment
                R.id.home -> {
                    this.supportFragmentManager.commit {
                        addToBackStack(HomeFragment::class.toString())
                        setReorderingAllowed(true)
                        replace(R.id.myNavHostFragment, HomeFragment())
                    }
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                //mostro lo show profile fragment
                R.id.profile -> {
                    // DEVO APRIRE IL FRAMMENTO CHE MOSTRA LO SHOW PROFILE FRAGMENT
                    this.supportFragmentManager.commit {
                        addToBackStack(ShowProfileFragment::class.toString())
                        setReorderingAllowed(true)
                        replace(R.id.myNavHostFragment, ShowProfileFragment())
                    }
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.listTimeSlot -> {
                    this.supportFragmentManager.commit {
                        addToBackStack(TimeSlotListFragment::class.toString())
                        setReorderingAllowed(true)
                        replace(R.id.myNavHostFragment, TimeSlotListFragment())
                    }
                   /* val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.containerFragment)*/
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.logout -> {
                   /* this.supportFragmentManager.commit {
                        addToBackStack(LoginActivity::class.toString())
                        setReorderingAllowed(true)
                        replace(R.id.myNavHostFragment, TimeSlotListFragment())
                        } */

                    val navController = findNavController(R.id.myNavHostFragment)
                     navController.navigate(R.id.loginActivity)
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true

                }

                else -> {
                    false
                }
            }
        }

        val view = nv.getHeaderView(0)
        email_field = view.findViewById(R.id.email_nv)
        name_field = view.findViewById(R.id.name_nv)
        image_field = view.findViewById(R.id.image_nv)

        profViewModel.email.observe(this) {
            Log.i("test_menu",it)
            if(it!=""){
                Log.i("test_menu","2")
                readData(it)
            }else{
                val id = intent.getStringExtra("id").toString()
                Log.i("test_menu",id)
                profViewModel.setEmail(id)
                readData(id)
            }

        }



    }
    private fun readData(id: String) {
       db.collection("users").document(id).get().addOnSuccessListener {
           Log.i("test_menu",it.data.toString())
           if (it.get("name").toString() != "null") {
               name_field.text = it.get("name").toString()
           }
                email_field.text = it.get("email").toString()
                if (it.get("photoString").toString() != "") {
                    uriImageString = it.get("photoString").toString()
                    val uriImage = Uri.parse(uriImageString)
                    image_field.setImageURI(uriImage)
                } else {
                    image_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
                }

        }.addOnFailureListener{e ->
             Log.i("test_menu","Error adding document",e)
        }



    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Full name", name)
        outState.putString("Email", email)
        outState.putString("Picture", uriImageString)
        super.onSaveInstanceState(outState)

    }

    /* function used for restoring fields' state */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val name = savedInstanceState.getString("Full name", "0")
        name_field.text = name
        val email = savedInstanceState.getString("Email", "0")
        email_field.text = email

        if (savedInstanceState.getString("Picture", "0") != "") {
            uriImageString = savedInstanceState.getString("Picture", "0")
            var uriImage = Uri.parse(uriImageString)
            image_field.setImageURI(uriImage)
        }

        super.onRestoreInstanceState(savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.main_menu, menu)
        //menuInflater.inflate(R.menu.options_menu,menu)
        //menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean{
        //if drawerIcon clicked
        if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            /*img.alpha=0.1F
            welcome_text.text = "I OPENED THE DRAWER"*/
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }

    }

}


