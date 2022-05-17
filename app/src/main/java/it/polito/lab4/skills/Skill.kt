package it.polito.lab4.skills

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Skill(
    var title: String,
    var description: String,
    var pos: Int,
    var user: String,
    var search: String = title.lowercase()
    ) : Parcelable {



    override fun toString(): String {
        return "$title###$description###$pos"
    }
}

