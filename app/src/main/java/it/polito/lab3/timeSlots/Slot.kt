package it.polito.lab3.timeSlots

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Slot(
    var title: String,
    var description: String,
) : Parcelable {
    constructor( title: String,
                description: String,
                 date: String,
                 duration: String,
                 location: String) : this(title, description)

    override fun toString(): String {
        return "$title###$description###"
    }
}