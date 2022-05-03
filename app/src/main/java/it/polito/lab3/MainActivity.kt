package it.polito.lab3

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import it.polito.lab3.fragments.ProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*


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
    private lateinit var uriImageString: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref =
            this.getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)
        //per gestire la visibilità del menu a sinistra e agganciare i listener
        drawerLayout = findViewById(R.id.my_drawer_layout)
        nv = findViewById(R.id.nav_view)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //email_field = view.findViewById(R.id.email_nv)


        //collego i listener al menu a sinistra che si apre
        nv.setNavigationItemSelectedListener {
            when (it.itemId) {
                //mostro l'home fragment
                R.id.home -> {
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.homeFragment)
                    true
                }
                //mostro lo show profile fragment
                R.id.profile -> {
                    // DEVO APRIRE IL FRAMMENTO CHE MOSTRA LO SHOW PROFILE FRAGMENT
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.showProfileFragment)
                    //Toast.makeText(this,"APRI SHOW PROFILE",Toast.LENGTH_SHORT).show()
                    true
                }
                //mostro il time slot details fragment
                R.id.details -> {
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.timeSlotDetailsFragment)
                    //Toast.makeText(this,"APRI TIME SLOT DETAILS",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.edit -> {
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.timeSlotEditFragment)
                    //Toast.makeText(this,"APRI TIME SLOT DETAILS",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.listTimeSlot -> {
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.containerFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
       var view = nv.getHeaderView(0)
        email_field = view.findViewById(R.id.email_nv)
        name_field = view.findViewById(R.id.name_nv)
        if(sharedPref.getString("id_name","Your name").toString() != "Full name"){
            name_field.text = sharedPref.getString("id_name","Your name").toString()
            name  = sharedPref.getString("id_name","Your name").toString()
        }
        if(sharedPref.getString("id_email","Your email").toString() != "email@address"){
            email_field.text = sharedPref.getString("id_email","Your email").toString()
            email  = sharedPref.getString("id_email","Your email").toString()
        }
        image_field = view.findViewById(R.id.image_nv)
       var uriImageString = sharedPref.getString("id_photo", "").toString()
        if(uriImageString!= "") {
            var uriImage = Uri.parse(uriImageString)
            image_field.setImageURI(uriImage)
        }else{
            image_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
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


