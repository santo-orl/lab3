package it.polito.lab3

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeSlotViewModel: ViewModel() {
    private val _title = MutableLiveData<String>("Title")
    val title: LiveData<String> = _title

    fun setTitle(desiredTitle: String) {
        _title.value = desiredTitle
    }

    fun hasNoTitleSet(): Boolean {
        return _title.value.isNullOrEmpty()
    }

}