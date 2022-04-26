package it.polito.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
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
                R.id.profile -> {
                    // DEVO APRIRE IL FRAMMENTO CHE MOSTRA LO SHOW PROFILE FRAGMENT
                    Toast.makeText(this,"APRI SHOW PROFILE",Toast.LENGTH_SHORT).show()
                    true
                }
                    //DEVO APRIRE IL FRAMMENTO CHE MI MOSTRA IL TIME_SLOT_DETAILS
                R.id.timeSlot -> {
                    Toast.makeText(this,"APRI TIME SLOT DETAILS",Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }


    override fun onOptionsItemSelected(item:MenuItem):Boolean{
        //if pencil clicked

        val img = findViewById<ImageView>(R.id.imageView)

        //if drawerIcon clicked
        if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            img.alpha = 0.5F
            return true
        }
        else {
            return super.onOptionsItemSelected(item)
        }


    }

}

