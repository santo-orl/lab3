package it.polito.lab3

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView

// Main activity, is the base for all the fragments
class MainActivity : AppCompatActivity(){

    //per gestire il menu che si apre a sinistra
    lateinit var nv : NavigationView
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


