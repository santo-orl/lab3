package it.polito.lab3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.lab3.skills.Skill
import it.polito.lab3.timeSlots.Slot

class TimeSlotViewModel: ViewModel() {
    private val _slots = MutableLiveData<ArrayList<Slot>>(arrayListOf())
    var slots: LiveData<ArrayList<Slot>> =_slots

    // private var l: ListenerRegistration
   /*  private val db: FirebaseFirestore
     init {
         db=FirebaseFirestore.getInstance()
     }
*/
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