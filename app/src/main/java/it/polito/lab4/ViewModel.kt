package it.polito.lab4

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Slot
import java.util.*


class ViewModel: ViewModel() {
    private lateinit var id : String
    private var storage = Firebase.storage
    var storageRef = storage.reference

    // var s = Storage.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _user = MutableLiveData<User>(User("", "", "", "", ""))
    private val _slot = MutableLiveData<Slot>(Slot("","","","","",-1,""))
    val slot: LiveData<Slot> = _slot

    private val _skill = MutableLiveData<String>("")
    val skill: LiveData<String> = _skill

    private val _description = MutableLiveData<String>("")
    val description : LiveData<String> = _description
    private val _nickname = MutableLiveData<String>("")
    val nickname: LiveData<String> = _nickname

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _photoString = MutableLiveData<String>("")
    var photoString: LiveData<String> = _photoString

    fun uploadImage(photoString: String): Uri {
        // Defining the child of storageReference
        val ref = storageRef.child(
            "images/"
                    + id.toString()
        )
        Log.i("test_vm","after ref ${ref.toString()}")
        var downloadUri: Uri = Uri.EMPTY
        if (photoString != "") {
            // adding listeners on upload
            // or failure of image
            var uploadTask = ref.putFile(Uri.parse(photoString))
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                Log.i("test_vm","after ref ${ref.downloadUrl.toString()}")
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("test_vm", "salvata immagine " + ref + " " + Uri.parse(photoString))
                    downloadUri = task.result
                } else {
                    Log.i("test_vm","Errore")
                }
            }.addOnFailureListener{e ->
                Log.i("test","Error adding document",e)
            }
        }
        Log.i("test_vm","after ref ${ref.downloadUrl.toString()}")
        return downloadUri
    }

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

    fun setSlot(new: Slot){
        _slot.value = new
    }

    fun setId(desiredId: String){
        id = desiredId
    }
    fun setEmail(desiredEmail: String){
        _email.value = desiredEmail
    }
    fun addSlot(new: Slot) {
        db.collection("skills").document(id).collection("slots")
            .add(new).addOnSuccessListener { documentReference ->
            Log.i("test", "DocumentSnapshot added with ID:${documentReference}")
        }
            .addOnFailureListener { e ->
                Log.i("test", "Error adding document", e)
            }

        db.collection("slots").add(new).addOnSuccessListener { documentReference ->
            Log.i("test", "DocumentSnapshot added with ID:${documentReference}")
        }
            .addOnFailureListener { e ->
                Log.i("test", "Error adding document", e)
            }
    }

    fun deleteSlot() {
        db.collection("slots").whereEqualTo("title",
            slot.value?.title
        ).whereEqualTo("description",slot.value?.description)
            .whereEqualTo("location",slot.value?.location)
            .whereEqualTo("date",slot.value?.date)
            .whereEqualTo("duration",slot.value?.duration).get().addOnSuccessListener {
                    result ->
                for (document in result)
                    document.reference.delete()
            }

        db.collection("skills").document(id).collection("slots").whereEqualTo("title",
            slot.value?.title
        ).whereEqualTo("description",slot.value?.description)
            .whereEqualTo("location",slot.value?.location)
            .whereEqualTo("date",slot.value?.date)
            .whereEqualTo("duration",slot.value?.duration).get().addOnSuccessListener {
                result ->
                for (document in result)
                    document.reference.delete()
            }
    }

    fun setSkill(desiredName: String) {
        _skill.value = desiredName
    }
    fun setDesc(desiredName: String) {
        _description.value = desiredName
    }
}