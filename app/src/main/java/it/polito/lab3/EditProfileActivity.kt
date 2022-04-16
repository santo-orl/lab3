package it.polito.lab3

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.os.SystemClock
import android.provider.MediaStore
import android.text.InputType.*
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

//Activity designed to let the user edit information
class EditProfileActivity : AppCompatActivity() {

    //fields
    lateinit var nameToUpdate: String
    lateinit var nicknameToUpdate: String
    lateinit var emailToUpdate: String
    lateinit var locationToUpdate: String
    private lateinit var profileUri: Uri
    private var uriImageString: String = ""

    val name:String = "Full name"
    private val nickname:String = "Nickname"
    private val location:String = "Location"
    private val email:String = "email@address"

    private  var skillList: ArrayList<Skill> =  arrayListOf()
    private lateinit var skillAdapter: Skill_Adapter


    lateinit var filePhoto: File
    lateinit var photo_button: ImageButton

    private val FILE_NAME = "photo.jpg"
    private val REQUEST_CODE_CAMERA = 13
    private val REQUEST_CODE_GALLERY = 15
    private lateinit var state: Parcelable



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        findViewById<EditText>(R.id.editName).hint = name
        findViewById<EditText>(R.id.editEmail).hint= email
        findViewById<EditText>(R.id.editNick).hint = nickname
        findViewById<EditText>(R.id.editLocation).hint = location
        photo_button = findViewById(R.id.imageButton)
        findViewById<EditText>(R.id.editEmail).inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_EMAIL_ADDRESS



        val intent = intent
        if(intent.getStringExtra("group19.lab2.NAME").toString()!= name ) {
            nameToUpdate = intent.getStringExtra("group19.lab2.NAME").toString()
            findViewById<EditText>(R.id.editName).setText(nameToUpdate)
        }
        if(intent.getStringExtra("group19.lab2.NICKNAME").toString()!= nickname ) {
            nicknameToUpdate = intent.getStringExtra("group19.lab2.NICKNAME").toString()
            findViewById<EditText>(R.id.editNick).setText(nicknameToUpdate)
        }
        if(intent.getStringExtra("group19.lab2.EMAIL").toString()!= email ) {
            emailToUpdate = intent.getStringExtra("group19.lab2.EMAIL").toString()
            findViewById<EditText>(R.id.editEmail).setText(emailToUpdate)
        }
        if(intent.getStringExtra("group19.lab2.LOCATION").toString()!= location ) {
            locationToUpdate = intent.getStringExtra("group19.lab2.LOCATION").toString()
            findViewById<EditText>(R.id.editLocation).setText(locationToUpdate)
        }
       if(intent?.getStringExtra("group19.lab2.ImageProfile")!="") {
            uriImageString = intent?.getStringExtra("group19.lab2.ImageProfile").toString()
            profileUri = Uri.parse(uriImageString)
            photo_button.setImageURI(profileUri)
        }else{
            photo_button.setImageResource(R.drawable.camera_icon_21)

       }
        var list =
            intent?.getParcelableArrayListExtra<Skill>("group19.lab2.SKILLS") as ArrayList<Skill>
        if(list.size>0){
            skillList = list
        }

        setUpLayout()
        photo_button.setOnClickListener {
            showPopUp(photo_button)
        }
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("Skills", skillList)
        outState.putString("Picture", uriImageString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getString("Picture", "") != "") {
            uriImageString = savedInstanceState.getString("Picture", "")
            profileUri = Uri.parse(uriImageString)
            photo_button.setImageURI(profileUri)
        } else {
            photo_button.setImageResource(R.drawable.camera_icon_21)
        }
            skillList = arrayListOf()
            skillList =
                savedInstanceState.getParcelableArrayList<Skill>("Skills") as ArrayList<Skill>
            setUpLayout()

    }

    //layout manager function
    private fun setUpLayout() {
        if(skillList.size == 0){
            skillList.add(Skill("", "", -1))
        }

        recycler.layoutManager = LinearLayoutManager(this)
        skillAdapter = Skill_Adapter(skillList)
        recycler.adapter = skillAdapter

        skillAdapter.setOnTodoDeleteClick(object : SkillUI.SkillListener {
            override fun onSkillDeleted(position: Int) {
                skillList.removeAt(position)
                skillAdapter.notifyDataSetChanged()
            }
        })
        btn_add_skill.setOnClickListener {
            val position = if (skillList.isEmpty()) 0 else skillList.size - 1
            skillList.add(Skill("", "",-1))
            skillAdapter.notifyItemInserted(position)
            skillAdapter.notifyDataSetChanged()
        }

    }


    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }



        fun showPopUp(view: ImageButton) {
        val popupMenu = PopupMenu(this, view, Gravity.BOTTOM)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.header_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.camera -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val photoFile: File = createImageFile()
                    val photoURI: Uri = getUriForFile(this, "com.example.android.fileprovider", photoFile)
                    takePictureIntent.putExtra("URI", photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
                    }

                R.id.gallery -> {
                    val pickPhotoIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhotoIntent, REQUEST_CODE_GALLERY)
                }
            }

            true
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            val cameraPic = data?.extras?.get("data") as Bitmap
            val uri = data.getStringExtra("URI")?.toUri()
            photo_button.setImageURI((uri))
            photo_button.setImageBitmap(cameraPic)
            profileUri= cameraPic.saveImage(this)!!
            uriImageString = profileUri.toString()

        }else{
            if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK)
                photo_button.setImageURI(data?.data)
                profileUri = data?.data!!
                uriImageString = profileUri.toString()
            contentResolver.takePersistableUriPermission(
                profileUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun Bitmap.saveImage(context: Context): Uri? {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

            val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(this, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)


                return uri
            }
        } else {
            val directory =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + separator + "test_pictures")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName =  "img_${SystemClock.uptimeMillis()}"+ ".jpeg"
            val file = File(directory, fileName)
            saveImageToStream(this, FileOutputStream(file))
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            return Uri.fromFile(file)
        }
        return null
    }


    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //function overridden to persist data if back button is pressed
    override fun onBackPressed() {
        val ii = Intent(this, MainActivity::class.java)

        ii.putExtra("group19.lab2.NAME", findViewById<EditText>(R.id.editName).text.toString())
        ii.putExtra("group19.lab2.NICKNAME", findViewById<EditText>(R.id.editNick).text.toString())
        ii.putExtra("group19.lab2.EMAIL", findViewById<EditText>(R.id.editEmail).text.toString())
        ii.putExtra(
            "group19.lab2.LOCATION",
            findViewById<EditText>(R.id.editLocation).text.toString()
        )
        val listProva = skillList.toMutableList() as ArrayList
        for (i in 0 until listProva.size) {
            if (listProva[i].title.length <= 1 || listProva[i].description.length <= 1) {
                skillList.removeAt(i)
            }
        }

            ii.putExtra("group19.lab2.SKILLS", skillList)

            if (uriImageString != "") {
                ii.putExtra("group19.lab2.ImageProfile", uriImageString)
            } else {
                ii.putExtra("group19.lab2.ImageProfile", "")
            }
            setResult(RESULT_OK, ii);
            super.onBackPressed()

            //ii.putExtra("group19.lab2.SKILLS",findViewById<EditText>(R.id.editTextTextSkills).text);

            //startActivity(ii);

        }


}
