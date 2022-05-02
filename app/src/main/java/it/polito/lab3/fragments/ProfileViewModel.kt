package it.polito.lab3.fragments

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {
    private val _name = MutableLiveData<String>("Full name")
    val name: LiveData<String> = _name

    fun setName(desiredName: String){
        _name.value = desiredName
    }
}