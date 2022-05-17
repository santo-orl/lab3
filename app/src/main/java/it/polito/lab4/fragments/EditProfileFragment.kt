package it.polito.lab4.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable

import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity

import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*

import androidx.fragment.app.activityViewModels

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import it.polito.lab4.ViewModel
import it.polito.lab4.R
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI
import it.polito.lab4.skills.Adapter_editProfile
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
    private lateinit var adapterEditProfile: Adapter_editProfile

    lateinit var filePhoto: File
    lateinit var photo_button: ImageButton

    private var storage = Firebase.storage("gs://timebankingapplication")
    var storageRef = storage.reference

    private val FILE_NAME = "photo.jpg"
    private val REQUEST_CODE_CAMERA = 13
    private val REQUEST_CODE_GALLERY = 15
    private lateinit var state: Parcelable
    private val db = FirebaseFirestore.getInstance()

    private val vm by activityViewModels<ViewModel>()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.setTitle("Edit profile")
        name_field = view.findViewById(R.id.editName)
        nickname_field = view.findViewById(R.id.editNick)
        email_field = view.findViewById(R.id.editEmail)
        location_field = view.findViewById(R.id.editLocation)
        photo_button = view.findViewById(R.id.imageButton)

        vm.email.observe(this.viewLifecycleOwner) {
            readData(it)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                saveInfo()
                //findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
                this@EditProfileFragment.activity?.supportFragmentManager?.popBackStack()

            }
        })
        //photo_button = view.findViewById(R.id.imageButton)
        photo_button.setOnClickListener {
            showPopUp(photo_button)
        }

        btn_save.setOnClickListener {
           saveInfo()
            this@EditProfileFragment.activity?.supportFragmentManager?.popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveInfo() {
        if(name_field.text.toString()==""){
            nameToUpdate = name
        }else {
            nameToUpdate = name_field.text.toString()
        }
        if(nickname_field.text.toString()==""){
            nicknameToUpdate = nickname
        }else {
            nicknameToUpdate = nickname_field.text.toString()
        }
        if(email_field.text.toString()==""){
            emailToUpdate = email
        }else {
            emailToUpdate = email_field.text.toString()
        }
        if(location_field.text.toString()==""){
            locationToUpdate = location
        }else {
            locationToUpdate = location_field.text.toString()
        }
      /*  if(uriImageString == ""){
            Log.i("test_edit", uriImageString)
            uriImageToUpdate = uriImageString
        }else{
            uriImageToUpdate = profileUri.toString()
        }*/
        val listNoEmpty: ArrayList<Skill> = arrayListOf()
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
        }
        val uri = vm.uploadImage(uriImageString).toString()
        Log.i("test_edit","after uri ${uri.toString()}")
        vm.createUser(nameToUpdate,nicknameToUpdate,emailToUpdate,locationToUpdate, uriImageString,listNoEmpty)

    }

    private fun readData(id: String) {
        db.collection("users").document(id).get().addOnSuccessListener {
            if (it.get("name").toString() != "Full name") {
                name_field.setText(it.get("name").toString())
            }
            email_field.setText(it.get("email").toString())

            if (it.get("photoString").toString() != "") {
                uriImageString = it.get("photoString").toString()
                val pathReference = storageRef.child("images/" + email_field.text.toString())
                val localFile = File.createTempFile("images", "jpg")

                pathReference.getFile(localFile).addOnSuccessListener {
                    // Local temp file has been created
                    val uriImage = Uri.parse(localFile.path)
                   // Log.i("test_show", localFile.path.toString())
                    imageButton.setImageURI(uriImage)
                }.addOnFailureListener {
                    // Handle any errors
                }

              //  Log.i("test_show", pathReference.toString())

            }
            if (it.get("nickname").toString() != "null") {
                nickname_field.setText(it.get("nickname").toString())
            }else{
                nickname_field.hint = "Nickname"
            }

            if (it.get("location").toString() != "null") {
                location_field.setText(it.get("location").toString())
            }else{
                location_field.hint = "Location"
            }

        }.addOnFailureListener { e ->
            Log.i("test_edit", "Error adding document", e)
        }

        db.collection("skills").document(id).get().addOnSuccessListener {
            skillList = arrayListOf()
            if (it.exists()) {
                it.data!!.forEach { (c,s) ->
                    s as HashMap<*,*>
                    skillList.add(Skill(s["title"].toString(),s["description"].toString(),s["pos"].toString().toInt(), id))
                }
                Log.i("test10", skillList.toString())

            }
            setUpLayout()
        }.addOnFailureListener { e ->
            Log.i("test_edit", "Error adding document", e)
        }
    }


    private fun setUpLayout() {
        if(skillList.size == 0){
            Log.i("test", "Entra?")
            skillList.add(Skill("", "", -1, ""))
        }
        recycler.layoutManager = LinearLayoutManager(this.activity)
        adapterEditProfile = Adapter_editProfile(skillList)
        recycler.adapter = adapterEditProfile

        adapterEditProfile.setOnTodoDeleteClick(object : SkillUI.SkillListener {
            override fun onSkillDeleted(position: Int)   {
                vm.removeSkill(skillList[position])
                skillList.removeAt(position)
                adapterEditProfile.notifyDataSetChanged()

            }

            override fun onSkillClick(position: Int) {
                TODO("Not yet implemented")
            }

        })

        btn_add_skill.setOnClickListener {
            val position = if (skillList.isEmpty()) 0 else skillList.size - 1
            skillList.add(Skill("", "",-1, ""))
            adapterEditProfile.notifyItemInserted(position)
            adapterEditProfile.notifyDataSetChanged()
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

        //vm.setPhoto(uriImageString)
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


