package it.polito.lab3.fragments

import android.net.Uri
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {
    private val _name = MutableLiveData<String>().also{it.value = "Full name"}
    val name: LiveData<String> = _name

    private val _nickname = MutableLiveData<String>().also{it.value = "Nickname"}
    val nickname: LiveData<String> = _nickname

    private val _email = MutableLiveData<String>().also{it.value = "email@address"}
    val email: LiveData<String> = _email

    private val _location = MutableLiveData<String>().also{it.value = "Location"}
    val location: LiveData<String> = _location

    private val _photoString = MutableLiveData<String>().also{it.value = ""}
    var photoString: LiveData<String> = _photoString

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
}