package it.polito.lab3.fragments

import android.net.Uri
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.lab3.skills.Skill

class ProfileViewModel: ViewModel() {
    private val _name = MutableLiveData<String>("Full name")
    val name: LiveData<String> = _name

    private val _nickname = MutableLiveData<String>("Nickname")
    val nickname: LiveData<String> = _nickname

    private val _email = MutableLiveData<String>("email@address")
    val email: LiveData<String> = _email

    private val _location = MutableLiveData<String>("Location")
    val location: LiveData<String> = _location

    private val _photoString = MutableLiveData<String>("")
    var photoString: LiveData<String> = _photoString

    private val _skills = MutableLiveData<ArrayList<Skill>>(arrayListOf())
    var skills: LiveData<ArrayList<Skill>> =_skills

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