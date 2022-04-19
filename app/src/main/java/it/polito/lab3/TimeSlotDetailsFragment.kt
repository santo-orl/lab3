package it.polito.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimeSlotDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    // TODO: Rename and change types of parameters
    var title: String = "Title"
    var description: String = "Description"
    var date: String = "Date"
    var time: String = "Duration"
    var location: String = "Location"
    lateinit var edit_button: Button

    lateinit var title_field: TextView
    lateinit var description_field: TextView
    lateinit var date_field: TextView
    lateinit var time_field: TextView
    lateinit var location_field: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("Title", "0")
            description = savedInstanceState.getString("Description", "0")
            date = savedInstanceState.getString("Date", "0")
            time = savedInstanceState.getString("Time", "0")
            location = savedInstanceState.getString("Location", "0")
        }

        title_field.text = title
        description_field.text = description
        date_field.text = date
        time_field.text = time
        location_field.text = location

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_slot_details, container, false)
    }


    private fun editAdv(): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        edit_button = view.findViewById(R.id.editButton)
        edit_button.setOnClickListener {
            findNavController().navigate(R.id.action_timeSlotDetailsFragment_to_timeSlotEditFragment)
          /*  val fragmentManager = fragmentManager
            if (fragmentManager != null) {
                //fragmentManager.beginTransaction().replace(R.id.fragment, this).commit()
            };*/
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("Title", title)
        outState.putString("Description", description)
        outState.putString("Date", date)
        outState.putString("Time", time)
        outState.putString("Location", location)

        //  outState.putParcelable("Skills", recycler.layoutManager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
        //outState.putString("Skills", skills)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeSlotDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimeSlotDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}