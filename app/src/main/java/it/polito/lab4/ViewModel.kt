package it.polito.lab4

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Slot
import java.util.*


class ViewModel: ViewModel() {
    private lateinit var id : String
    private var storage = Firebase.storage("gs://timebankingapplication")
    var storageRef = storage.reference

    // var s = Storage.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    /*private val _user = MutableLiveData<User>(User("", "", "", "", ""))
    private val _slot = MutableLiveData<Slot>(Slot("","","","","",-1,"",""))*/
    private val _user = MutableLiveData<User>(User("", "", "", "", "",0))
    private val _slot = MutableLiveData<Slot>(Slot("","","","","",-1,"",""))

    val slot: LiveData<Slot> = _slot

    private val _skill = MutableLiveData<String>("")
    val skill: LiveData<String> = _skill

    private val _description = MutableLiveData<String>("")
    val description : LiveData<String> = _description

    //hour used as credit
    private val _hours = MutableLiveData<Int>(0)
    val hour : LiveData<Int> = _hours

    private val _nickname = MutableLiveData<String>("")
    val nickname: LiveData<String> = _nickname

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _photoString = MutableLiveData<String>("")
    var photoString: LiveData<String> = _photoString

    /*fun uploadImage(photoString: String): Uri {
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
    }*/

    fun createUser(name:String, nickname: String, email: String,
                    location: String,
                     photoString: String,  skills: ArrayList<Skill> ){
            for(s in skills){
                if(s.id!=""){
                    Log.i("test_nonSsegnata", s.toString())
                    db.collection("skills").document(s.id)
                        .set(s, SetOptions.merge()).addOnSuccessListener{ documentReference ->
                        Log.i("test", "DocumentSnapshot added with ID:${documentReference}")
                    }
                        .addOnFailureListener { e ->
                            Log.i("test", "Error adding document", e)
                        }
                }else {
                    var slotRef = db.collection("skills").document()
                    s.reference(slotRef.id)
                    Log.i("test_Segnata", s.toString())
                    slotRef.set(s, SetOptions.merge()).addOnSuccessListener { documentReference ->
                        Log.i("test", "DocumentSnapshot added with ID:${documentReference}")
                    }
                        .addOnFailureListener { e ->
                            Log.i("test", "Error adding document", e)
                        }
                }
            }
          /*  db.collection("skills").document(email).set(map).addOnSuccessListener { documentReference ->
                Log.i("test","DocumentSnapshot added with ID:${documentReference}")
            }
                .addOnFailureListener{e ->
                    Log.i("test","Error adding document",e)
                }*/
        val user = User(name, nickname, email, location, photoString,0)
        _user.value = user
        id = email
        db.collection("users").document(email).set(user, SetOptions.merge()).addOnSuccessListener { documentReference ->
            Log.i("test","DocumentSnapshot added with ID:${documentReference}")
        }
            .addOnFailureListener{e ->
                Log.i("test","Error adding document",e)
            }
    }

    fun setSlot(new: Slot){
        _slot.value = new
    }

    fun setHours(hours: Int){
        _hours.value= hours
    }

    fun setId(desiredId: String){
        id = desiredId
    }
    fun setEmail(desiredEmail: String){
        _email.value = desiredEmail
    }
    fun addSlot(new: Slot) {
        if(new.id!=""){
            db.collection("slots").document(new.id).set(new, SetOptions.merge()).addOnSuccessListener { documentReference ->
                Log.i("test", "DocumentSnapshot added with ID:${documentReference}")
            }
                .addOnFailureListener { e ->
                    Log.i("test", "Error adding document", e)
                }
        }else {
            var slotRef = db.collection("slots").document()
            new.reference(slotRef.id)
            slotRef.set(new, SetOptions.merge()).addOnSuccessListener { documentReference ->
                Log.i("test", "DocumentSnapshot added with ID:${documentReference}")
            }
                .addOnFailureListener { e ->
                    Log.i("test", "Error adding document", e)
                }
        }
    }

    fun deleteSlot(slot: Slot) {
        Log.i("TESTVM", slot.id)
        db.collection("slots").document(slot.id).get().addOnSuccessListener {
            Log.i("TESTVM", it.data.toString())
            it.reference.delete()

            }

    /*    db.collection("skills").document(id).collection("slots").whereEqualTo("title",
            slot.value?.title
        ).whereEqualTo("description",slot.value?.description)
            .whereEqualTo("location",slot.value?.location)
            .whereEqualTo("date",slot.value?.date)
            .whereEqualTo("duration",slot.value?.duration).get().addOnSuccessListener {
                result ->
                for (document in result)
                    document.reference.delete()
            }*/
    }

    fun setSkill(desiredName: String) {
        _skill.value = desiredName
    }
    fun setDesc(desiredName: String) {
        _description.value = desiredName
    }

    fun removeSkill(skill: Skill) {
        db.collection("skills").whereEqualTo("id",skill.id).get().addOnSuccessListener { result ->
            for (document in result)
                document.reference.delete()
        }

        db.collection("slots").whereEqualTo("user", skill.user).whereEqualTo("title", skill.title)
            .whereEqualTo("description", skill.description).get().addOnSuccessListener { result ->
                for (document in result)
                    document.reference.delete()
            }


        }


    }
