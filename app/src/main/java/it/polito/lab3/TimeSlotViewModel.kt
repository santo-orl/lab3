package it.polito.lab3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeSlotViewModel: ViewModel() {
    val title = MutableLiveData<String>().also{
        it.value = "Title"
    }

    fun editTitle(title_text: String){
        title.value = title_text
    }

}