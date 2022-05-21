package it.polito.lab4
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var name: String, var nickname: String, var email: String,
                var location: String,
                var  photoString: String,
                var hours: Int
                //var skills: ArrayList<Skill>
) : Parcelable {

    override fun toString(): String {
        return ""
    }

    constructor():this("","","","","",2)

}
