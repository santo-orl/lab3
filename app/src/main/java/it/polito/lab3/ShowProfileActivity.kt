package it.polito.lab3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_edit_profile.*

class ShowProfileActivity : AppCompatActivity() {

    //to make the persistency unique
    private val sharedPrefFIle = "it.polito.showprofileactivityy"

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
    lateinit var sharedPref:SharedPreferences;

    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null


    /* function called each time the activity is creater or re-opened */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        //Code referring to DrawerMenu
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //persistency
        sharedPref = this.getSharedPreferences(sharedPrefFIle, Context.MODE_PRIVATE)

        photo_field = findViewById<ImageView>(R.id.imageView)
        photo_field.setImageResource(R.drawable.default_user_profile_picture_hvoncb)
        name_field = findViewById<TextView>(R.id.name)
        name = sharedPref.getString("id_name","Full name").toString()
        name_field.text = name
        nickname_field = findViewById<TextView>(R.id.nickname)
        nickname = sharedPref.getString("id_nickname","Nickname").toString()
        nickname_field.text = nickname
        location_field = findViewById<TextView>(R.id.location)
        location = sharedPref.getString("id_location","Location").toString()
        location_field.text = location
        email_field = findViewById<TextView>(R.id.email)
        email =  sharedPref.getString("id_email", "email@address").toString()
        email_field.text =email
        uriImageString = sharedPref.getString("id_photo", "").toString()
        if(uriImageString!= "") {
            uriImage = Uri.parse(uriImageString)
            photo_field.setImageURI(uriImage)
        }
        var skills = sharedPref.getString("id_skills","")
        if(skills!= "") {
            var ll = skills!!.split("&&&")
            for(s in ll){
                var skillItem = s.split("###")
                skillList.add(Skill(skillItem[0], skillItem[1],skillItem[2].toInt()))
            }
            recycler.layoutManager = LinearLayoutManager(this)
            adapterText = Adapter_Text(skillList)
            recycler.adapter = adapterText
        }

    }

    /* function used to add the pencil icon
       in order to activate the right hand-side menu
    */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /* function used to make the pencil menu icon and drawer menu responsive
       when clicked
    */
    override fun onOptionsItemSelected(item:MenuItem):Boolean{
        //if pencil clicked
        if(item.itemId.equals(R.id.pencil)){
            editProfile()
            return true
        }
        //if drawerIcon clicked
        if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            return true
        }
        else return super.onOptionsItemSelected(item)
    }

    /* function used for saving fields' state */
    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("Full name", name)
        outState.putString("Nickname", nickname)
        outState.putString("Email", email)
        outState.putString("Location", location)
        outState.putString("Picture", uriImageString)
        outState.putParcelableArrayList("Skills", skillList)
        super.onSaveInstanceState(outState)
    }

    /* function used for restoring fields' state */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

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

        recycler.layoutManager = LinearLayoutManager(this)
        adapterText = Adapter_Text(skillList)
        recycler.adapter = adapterText

        super.onRestoreInstanceState(savedInstanceState)
    }

    //to be invoked when the pencil menu icon is clicked
    private fun editProfile() {

        val i = Intent(this, EditProfileActivity::class.java);
        i.putExtra("group19.lab2.NAME", name_field.text.toString())
        i.putExtra("group19.lab2.NICKNAME", nickname_field.text.toString())
        i.putExtra("group19.lab2.EMAIL", email_field.text.toString())
        i.putExtra("group19.lab2.LOCATION", location_field.text.toString())
        i.putExtra("group19.lab2.ImageProfile", uriImageString)
        i.putExtra("group19.lab2.SKILLS", skillList)
        startActivityForResult(i, 25);

    }

    /* receiving and managing data from other intents.. */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 25 && resultCode == RESULT_OK) {
            if (data?.getStringExtra("group19.lab2.NAME") != "") {
                name_field.text = data?.getStringExtra("group19.lab2.NAME")
                name = name_field.text.toString()
            } else {
                findViewById<TextView>(R.id.name).text = name
            }
            if (data?.getStringExtra("group19.lab2.NICKNAME") != "") {
                nickname_field.text = data?.getStringExtra("group19.lab2.NICKNAME")
                nickname = nickname_field.text.toString()
            } else {
                findViewById<TextView>(R.id.nickname).text = nickname
            }
            if (data?.getStringExtra("group19.lab2.EMAIL") != "") {
                email_field.text = data?.getStringExtra("group19.lab2.EMAIL")
                email = email_field.text.toString()
            } else {
                findViewById<TextView>(R.id.email).text = email
            }
            if (data?.getStringExtra("group19.lab2.LOCATION") != "") {
                location_field.text = data?.getStringExtra("group19.lab2.LOCATION")
                location = location_field.text.toString()
            } else {
                findViewById<TextView>(R.id.location).text = location
            }
            if (data?.getStringExtra("group19.lab2.ImageProfile").toString() != "") {
                uriImageString = data?.getStringExtra("group19.lab2.ImageProfile").toString()
                uriImage = Uri.parse(uriImageString)
                findViewById<ImageView>(R.id.imageView).setImageURI(uriImage)
            }

            skillList = arrayListOf()
            skillList =
                data?.getParcelableArrayListExtra<Skill>("group19.lab2.SKILLS") as ArrayList<Skill>

            recycler.layoutManager = LinearLayoutManager(this)
            adapterText = Adapter_Text(skillList)
            recycler.adapter = adapterText

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /* plus option on pressing back button: make fields persistent */
    override fun onBackPressed() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString("id_name", name_field.text.toString())
        editor.putString("id_nickname", nickname_field.text.toString())
        editor.putString("id_location", location_field.text.toString())
        editor.putString("id_email", email_field.text.toString())
        editor.putString("id_photo", uriImageString)
        var ss = skillList.joinToString("&&&")
        editor.putString("id_skills",ss)
        editor.apply();

        super.onBackPressed()
    }
}