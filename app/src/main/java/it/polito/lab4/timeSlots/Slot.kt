package it.polito.lab4.timeSlots

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Slot(
    var title: String, var description: String, var date: String,
    var duration: String,
    var location: String,
    val pos: Int,
    val user: String
) : Parcelable {
 var id =""
    override fun toString(): String {
        return "$title###$description###$date###$duration###$pos###$location###$user"
    }
    fun reference(documentReference: String) {
        id = documentReference
    }

}