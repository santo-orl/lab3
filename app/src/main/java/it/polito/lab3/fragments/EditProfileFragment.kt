package it.polito.lab3.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import it.polito.lab3.R
import it.polito.lab3.skills.Skill
import it.polito.lab3.skills.Skill_Adapter
import java.io.File
import java.util.ArrayList

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    // TODO: Rename and change types of parameters
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
}
