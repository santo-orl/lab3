package it.polito.lab3
import android.os.Parcelable
import it.polito.lab3.skills.Skill
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var name: String, var nickname: String, var email: String,
                var location: String,
                var  photoString: String,
                //var skills: ArrayList<Skill>
) : Parcelable {

    override fun toString(): String {
        return ""
    }

}
