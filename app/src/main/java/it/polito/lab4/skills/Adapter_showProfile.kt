package it.polito.lab4.skills

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab4.R
import kotlinx.android.synthetic.main.skill_text_layout.view.*

class Adapter_showProfile(private val dataSet: ArrayList<Skill>) :
    RecyclerView.Adapter<Adapter_showProfile.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val number: TextView = view.skill_number_label2
        val sTitle: TextView = view.Title
        val sDesc: TextView = view.Description
        private lateinit var listener: AdapterView.OnItemClickListener

        init {

                view.setOnClickListener{

                }


        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.skill_text_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(dataSet[position].title != "" && dataSet[position].description != "") {
            val s1 = "Skill " + (position+1)
            val s2 = "Title: " + dataSet[position].title
            val s3 = "Description: " + dataSet[position].description
            viewHolder.number.text = s1
            viewHolder.sTitle.text = s2
            viewHolder.sDesc.text = s3
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}