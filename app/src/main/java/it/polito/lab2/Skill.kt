package it.polito.lab2

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Skill(
    var title: String,
    var description: String,
    var pos: Int
    ) : Parcelable {
    override fun toString(): String {
        return "$title###$description###$pos"
    }
}

