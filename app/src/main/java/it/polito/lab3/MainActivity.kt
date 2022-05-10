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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationView
import it.polito.lab3.fragments.ShowProfileFragment
import it.polito.lab3.fragments.TimeSlotListFragment


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





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("509920197029-bei5glek4tfdpmf81ertqddtv7kbivka.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()*/

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
                    this.supportFragmentManager.commit {
                        addToBackStack(HomeFragment::class.toString())
                        setReorderingAllowed(true)
                        replace(R.id.myNavHostFragment, HomeFragment())
                    }
              /*     val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.homeFragment)*/
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
                 /*   val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.showProfileFragment)*/
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    //Toast.makeText(this,"APRI SHOW PROFILE",Toast.LENGTH_SHORT).show()
                    true
                }
                //mostro il time slot details fragment
               /* R.id.details -> {
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.timeSlotDetailsFragment)
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    //Toast.makeText(this,"APRI TIME SLOT DETAILS",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.edit -> {
                    val navController = findNavController(R.id.myNavHostFragment)
                    navController.navigate(R.id.timeSlotEditFragment)
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    //Toast.makeText(this,"APRI TIME SLOT DETAILS",Toast.LENGTH_SHORT).show()
                    true
                }*/
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

        profViewModel.photoString.observe(this) {
            if(it!=""){
                var uriImage = Uri.parse(it)
        //        image_field.setImageURI(uriImage)
            }else {
                var uriImageString = sharedPref.getString("id_photo", "").toString()
                if(uriImageString!= "") {
                    var uriImage = Uri.parse(uriImageString)
//                    image_field.setImageURI(uriImage)
                }else{
                    image_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb) //default pic
                }
            }//default pic
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


