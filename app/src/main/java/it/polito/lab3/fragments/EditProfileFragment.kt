package it.polito.lab3.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable

import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher

import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.skills.Adapter_Text
import it.polito.lab3.skills.Skill
import it.polito.lab3.skills.SkillUI
import it.polito.lab3.skills.Skill_Adapter
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.btn_add_skill
import kotlinx.android.synthetic.main.activity_edit_profile.recycler
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    lateinit var name_field: EditText
    lateinit var nickname_field: EditText
    lateinit var email_field: EditText
    lateinit var location_field: EditText

    lateinit var nameToUpdate: String
    lateinit var nicknameToUpdate: String
    lateinit var emailToUpdate: String
    lateinit var locationToUpdate: String
    lateinit var uriImageToUpdate: String
    private lateinit var profileUri: Uri
    private var uriImageString: String = ""
    private var def_uriImageString: String = ""

    val name: String = "Full name"
    private val nickname: String = "Nickname"
    private val location: String = "Location"
    private val email: String = "email@address"

    private var skillList: ArrayList<Skill> = arrayListOf()
    private lateinit var skillAdapter: Skill_Adapter

    lateinit var filePhoto: File
    lateinit var photo_button: ImageButton

    private val FILE_NAME = "photo.jpg"
    private val REQUEST_CODE_CAMERA = 13
    private val REQUEST_CODE_GALLERY = 15
    private lateinit var state: Parcelable

    private val profViewModel by activityViewModels<ProfileViewModel>()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.setTitle("Edit profile")
        name_field = view.findViewById(R.id.editName)
        nickname_field = view.findViewById(R.id.editNick)
        email_field = view.findViewById(R.id.editEmail)
        location_field = view.findViewById(R.id.editLocation)
        photo_button = view.findViewById(R.id.imageButton)

        profViewModel.name.observe(this.viewLifecycleOwner){
            if(it != "" && it != name) {
                name_field.setText(it.toString())
            }
        }

        profViewModel.nickname.observe(this.viewLifecycleOwner){
            if(it != ""  && it!= nickname) {
                nickname_field.setText(it.toString())
            }
        }

        profViewModel.email.observe(this.viewLifecycleOwner){
            if(it != "" && it!= email) {
                email_field.setText(it.toString())
            }
        }

        profViewModel.location.observe(this.viewLifecycleOwner){
            if(it != "" && it!= location) {
                location_field.setText(it.toString())
            }
        }

        profViewModel.photoString.observe(this.viewLifecycleOwner){
            if(it!= "") {
                uriImageString = it
                profileUri = Uri.parse(it)
                photo_button.setImageURI(profileUri)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                //Log.i("test488", uriImageString)

                /*var ss = skillList.joinToString("&&&")
                editor.putString("id_skills",ss)

                */
                if(name_field.text.toString()==""){
                    profViewModel.setName(name)
                }else {
                    nameToUpdate = name_field.text.toString()
                    profViewModel.setName(nameToUpdate)
                }
                if(nickname_field.text.toString()==""){
                    profViewModel.setNickname(nickname)
                }else {
                    nicknameToUpdate = nickname_field.text.toString()
                    profViewModel.setNickname(nicknameToUpdate)
                    //editor.putString("id_nickname", nickname_field.text.toString())
                }
                if(email_field.text.toString()==""){
                    profViewModel.setEmail(email)
                }else {
                    emailToUpdate = email_field.text.toString()
                    profViewModel.setEmail(emailToUpdate)
                    //editor.putString("id_email", email_field.text.toString())
                }
                if(location_field.text.toString()==""){
                    profViewModel.setLocation(location)
                }else {
                    locationToUpdate = location_field.text.toString()
                    profViewModel.setLocation(locationToUpdate)
                    //editor.putString("id_location", location_field.text.toString())
                }
                if(uriImageString == ""){
                    profViewModel.setPhoto("")
                }else{
                    uriImageToUpdate = profileUri.toString()
                    profViewModel.setPhoto(uriImageToUpdate)
                    //editor.putString("id_photo", uriImageString)
                }
                var listNoEmpty: ArrayList<Skill> = arrayListOf()
                if(skillList.isNotEmpty()) {

                      for (s in skillList) {
                        if(s.title.length >= 5 && s.description.length >= 10){
                            Log.i("test", s.toString())
                             listNoEmpty.add(s)
                         }else if(s.title.length < 5){
                            Toast.makeText(activity,"Sorry, the title must be at least of 5 characters",Toast.LENGTH_SHORT).show()
                        }else if(s.description.length < 10){
                            Toast.makeText(activity,"Sorry, the description must be at least of 10 characters",Toast.LENGTH_SHORT).show()
                        }

                     }
                    profViewModel.setSkills(listNoEmpty)
                }else{
                    profViewModel.setSkills(arrayListOf())
                }
                profViewModel.createUser(nameToUpdate,nicknameToUpdate,emailToUpdate,locationToUpdate, uriImageToUpdate,listNoEmpty)
                //findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
                this@EditProfileFragment.activity?.supportFragmentManager?.popBackStack()

            }
        })
        profViewModel.skills.observe(this.viewLifecycleOwner){
            if (it.isNotEmpty()){
                skillList = it
                Log.i("test", skillList[0].toString())
            }
            setUpLayout()
        }

        //photo_button = view.findViewById(R.id.imageButton)
        photo_button.setOnClickListener {
            showPopUp(photo_button)
        }


        btn_save.setOnClickListener {
            if (name_field.text.toString() == "") {
                profViewModel.setName(name)
            } else {
                profViewModel.setName(name_field.text.toString())
            }
            if (nickname_field.text.toString() == "") {
                profViewModel.setNickname(nickname)
            } else {
                profViewModel.setNickname(nickname_field.text.toString())
                //editor.putString("id_nickname", nickname_field.text.toString())
            }
            if (email_field.text.toString() == "") {
                profViewModel.setEmail(email)
            } else {
                profViewModel.setEmail(email_field.text.toString())
                //editor.putString("id_email", email_field.text.toString())
            }
            if (location_field.text.toString() == "") {
                profViewModel.setLocation(location)
            } else {
                profViewModel.setLocation(location_field.text.toString())
                //editor.putString("id_location", location_field.text.toString())
            }
            if (uriImageString == "") {
                profViewModel.setPhoto("")
            } else {
                profViewModel.setPhoto(profileUri.toString())
                //editor.putString("id_photo", uriImageString)
            }

            if (skillList.isNotEmpty()) {
                var listNoEmpty: ArrayList<Skill> = arrayListOf()
                for (s in skillList) {
                    if (s.title.length >= 5 && s.description.length >= 10) {
                        Log.i("test", s.toString())
                        listNoEmpty.add(s)
                    } else if (s.title.length < 5) {
                        Toast.makeText(
                            activity,
                            "Sorry, the title must be at least of 5 characters",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (s.description.length < 10) {
                        Toast.makeText(
                            activity,
                            "Sorry, the description must be at least of 10 characters",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                profViewModel.setSkills(listNoEmpty)
            } else {
                profViewModel.setSkills(arrayListOf())
            }
            this@EditProfileFragment.activity?.supportFragmentManager?.popBackStack()
        }


        super.onViewCreated(view, savedInstanceState)
    }



    private fun setUpLayout() {
        if(skillList.size == 0){
            Log.i("test", "Entra?")
            skillList.add(Skill("", "", -1))
        }
        recycler.layoutManager = LinearLayoutManager(this.activity)
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
        val storageDir: File? = this.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        val popupMenu = PopupMenu(this.requireContext(), view, Gravity.BOTTOM)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.header_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.camera -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val photoFile: File = createImageFile()
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this.requireContext(),
                        "com.example.android.fileprovider",
                        photoFile
                    )
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
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == AppCompatActivity.RESULT_OK) {
            val cameraPic = data?.extras?.get("data") as Bitmap
            val uri = data.getStringExtra("URI")?.toUri()
            photo_button.setImageURI((uri))
            photo_button.setImageBitmap(cameraPic)
            profileUri= cameraPic.saveImage(this.requireContext())!!
            uriImageString = profileUri.toString()

        }else{
            if(requestCode == REQUEST_CODE_GALLERY && resultCode == AppCompatActivity.RESULT_OK)
                photo_button.setImageURI(data?.data)
            profileUri = data?.data!!
            uriImageString = profileUri.toString()
            this.requireContext().contentResolver.takePersistableUriPermission(
                profileUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        profViewModel.setPhoto(uriImageString)
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

       // name_field.doAfterTextChanged { editable-> if(editable!=null)
      //      profViewModel.setName(editable.toString()) }



                return uri
            }
        } else {
            val directory =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + "test_pictures")
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Full name", name_field.text.toString())
        outState.putString("Nickname", nickname_field.text.toString())
        outState.putString("Email", email_field.text.toString())
        outState.putString("Location", location_field.text.toString())
        outState.putString("Picture", uriImageString)
        outState.putParcelableArrayList("Skills", skillList)

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            name_field.setText(savedInstanceState.getString("Full name","0"))
            nickname_field.setText(savedInstanceState.getString("Nickname","0"))
            email_field.setText(savedInstanceState.getString("Email","0"))
            location_field.setText(savedInstanceState.getString("Location","0"))
        }
    }



}//class


