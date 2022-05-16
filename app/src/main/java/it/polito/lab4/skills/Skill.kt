package it.polito.lab4.skills

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Skill(
    var title: String,
    var description: String,
    var pos: Int
    ) : Parcelable {

    var search = title.lowercase()

    override fun toString(): String {
        return "$title###$description###$pos"
    }
}

