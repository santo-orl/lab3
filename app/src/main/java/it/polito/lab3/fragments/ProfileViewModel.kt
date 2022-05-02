package it.polito.lab3.fragments

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {
    val name = MutableLiveData<String>().also{it.value = "PROVA"}
    //val name: LiveData<String> = _name

    fun setName(desiredName: String){
        name.value = desiredName
    }
}