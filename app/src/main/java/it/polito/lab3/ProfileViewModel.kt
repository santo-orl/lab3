package it.polito.lab3

import android.net.Uri
import android.provider.ContactsContract
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab3.User
import it.polito.lab3.skills.Skill

class ProfileViewModel: ViewModel() {
    private val db: FirebaseFirestore
    init {
        db= FirebaseFirestore.getInstance()
    }
    private val _name = MutableLiveData<String>("")
    val name: LiveData<String> = _name

    private val _nickname = MutableLiveData<String>("")
    val nickname: LiveData<String> = _nickname

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _location = MutableLiveData<String>("")
    val location: LiveData<String> = _location

    private val _photoString = MutableLiveData<String>("")
    var photoString: LiveData<String> = _photoString

    private val _skills = MutableLiveData<ArrayList<Skill>>(arrayListOf())
    var skills: LiveData<ArrayList<Skill>> =_skills

    fun createUser(name:String, nickname: String, email: String,
                    location: String,
                     photoString: String,  skills: ArrayList<Skill> ){
        val user = User(name, nickname, email, location, photoString,  skills)
        db.collection("users").add(user).addOnSuccessListener { documentReference ->
            Log.i("test","DocumentSnapshot added with ID:${documentReference.id}")
        }
            .addOnFailureListener{e ->
                Log.i("test","Error adding document",e)
            }
    }
    fun setName(desiredName: String){
        _name.value = desiredName
    }

    fun setNickname(desiredNickname: String){
        _nickname.value = desiredNickname
    }

    fun setEmail(desiredEmail: String){
        _email.value = desiredEmail
    }

    fun setLocation(desiredLocation: String){
        _location.value = desiredLocation
    }

    fun setPhoto(desiredUri: String){
        _photoString.value = desiredUri
    }
    fun setSkills(desiredSkills:ArrayList<Skill>){
        _skills.value = ArrayList(desiredSkills)

    }
}