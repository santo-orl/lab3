package it.polito.lab3.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import android.widget.ImageButton

import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.skills.Skill
import it.polito.lab3.skills.SkillUI
import it.polito.lab3.skills.Skill_Adapter
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    lateinit var name_field: TextView


    lateinit var nameToUpdate: String
    lateinit var nicknameToUpdate: String
    lateinit var emailToUpdate: String
    lateinit var locationToUpdate: String
    private lateinit var profileUri: Uri
    private var uriImageString: String = ""

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name_field = view.findViewById(R.id.editName)

        profViewModel.name.observe(this.viewLifecycleOwner){
            if(it!= name) {
                name_field.text = it
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                profViewModel.setName(name_field.text.toString())
                findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
            }
        })
        //profViewModel.setName("CAMBIO")
        //Log.i("test1", name_field.text.toString())

       /* name_field.addTextChangedListener(object : TextWatcher {
            var check = false
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun afterTextChanged(editable: Editable) {
                if (check) {
                    Log.i("test", "check")
                    return
                }else{
                    check = true
                    Log.i("test1", name_field.text.toString())
                    profViewModel.setName(name_field.text.toString())
                }
            }

        })*/



        setUpLayout()
        photo_button = view.findViewById(R.id.imageButton)
        photo_button.setOnClickListener {
            showPopUp(photo_button)
        }


    }

    private fun setUpLayout() {
        if(skillList.size == 0){
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

}//class


