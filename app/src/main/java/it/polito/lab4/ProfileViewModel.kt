package it.polito.lab4

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Slot

class ProfileViewModel: ViewModel() {
    private lateinit var id : String
    private val db: FirebaseFirestore
    init {
        db= FirebaseFirestore.getInstance()
    }
    private val _user = MutableLiveData<User>(User("","","","","",))
    val user: LiveData<User> = _user
    private val _slot = MutableLiveData<String>("")
    val slot: LiveData<String> = _slot

    private val _skill = MutableLiveData<String>("")
    val skill: LiveData<String> = _skill

    private val _nickname = MutableLiveData<String>("")
    val nickname: LiveData<String> = _nickname

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _photoString = MutableLiveData<String>("")
    var photoString: LiveData<String> = _photoString

    fun createUser(name:String, nickname: String, email: String,
                    location: String,
                     photoString: String,  skills: ArrayList<Skill> ){
            val map: MutableMap<String, Skill> = HashMap()
            for(s in skills){
                map[s.title] = s
            }
            db.collection("skills").document(email).set(map).addOnSuccessListener { documentReference ->
                Log.i("test","DocumentSnapshot added with ID:${documentReference}")
            }
                .addOnFailureListener{e ->
                    Log.i("test","Error adding document",e)
                }
        val user = User(name, nickname, email, location, photoString)
        _user.value = user
        id = email
        db.collection("users").document(email).set(user).addOnSuccessListener { documentReference ->
            Log.i("test","DocumentSnapshot added with ID:${documentReference}")
        }
            .addOnFailureListener{e ->
                Log.i("test","Error adding document",e)
            }
    }

    fun setSlot(desiredName: String){
        _slot.value = desiredName
    }

    fun setId(desiredId: String){
        id = desiredId
    }
    fun setEmail(desiredEmail: String){
        _email.value = desiredEmail
    }

    fun setPhoto(desiredUri: String){
        _photoString.value = desiredUri
    }


    fun addSlot(s: Slot) {
        val map: MutableMap<String, Slot> = HashMap()
            map[s.title] = s

        db.collection("slots").document(id).set(map).addOnSuccessListener { documentReference ->
            Log.i("test","DocumentSnapshot added with ID:${documentReference}")
        }
            .addOnFailureListener{e ->
                Log.i("test","Error adding document",e)
            }

    }

    fun setSkill(desiredName: String) {
        _skill.value = desiredName
    }
}