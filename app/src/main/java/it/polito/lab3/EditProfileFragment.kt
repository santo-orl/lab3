package it.polito.lab3

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import java.io.File
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    // TODO: Rename and change types of parameters
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


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}