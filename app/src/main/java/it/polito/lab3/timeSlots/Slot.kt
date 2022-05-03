package it.polito.lab3.timeSlots

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Slot(var title: String, var description: String, var date: String,
                var duration: String,
               var  location: String
) : Parcelable {

    override fun toString(): String {
        return "$title###$description###$date###$duration###$location"
    }

}