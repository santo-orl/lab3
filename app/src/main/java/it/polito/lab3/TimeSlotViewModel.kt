package it.polito.lab3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.lab3.skills.Skill
import it.polito.lab3.timeSlots.Slot

class TimeSlotViewModel: ViewModel() {
    private val _slots = MutableLiveData<ArrayList<Slot>>(arrayListOf())
    var slots: LiveData<ArrayList<Slot>> =_slots

    fun setSlots(desiredSlots:ArrayList<Slot>){
        _slots.value = ArrayList(desiredSlots)
    }
    fun setSlot(desiredSlot:Slot){
        _slots.value?.add(desiredSlot)
    }
    fun remove(desiredSlot: Slot){
        _slots.value?.remove(desiredSlot)
    }


}