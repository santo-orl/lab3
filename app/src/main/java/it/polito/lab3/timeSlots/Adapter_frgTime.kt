package it.polito.lab3.timeSlots

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab3.R
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import kotlinx.android.synthetic.main.fragment_time_slot_details.view.*
import kotlinx.android.synthetic.main.fragment_time_slot_details.view.editDescription
import kotlinx.android.synthetic.main.fragment_time_slot_details.view.editTitle


class Adapter_frgTime (private val dataSet: ArrayList<Slot>) :
RecyclerView.Adapter<Adapter_frgTime.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.title_itemList
        val description:  TextView = view.description_itemList
        val cardView: CardView =  itemView.findViewById(R.id.card_list)
        init {
            cardView.setOnClickListener {
                Log.i("test2","Vede il click")
                cardView.findNavController().navigate(R.id.action_itemListFragment_to_timeSlotDetailsFragment)
            }

        }
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_item_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       if(dataSet[0].title == "No advertisement" && dataSet[0].description == "Click on the button below to add your first advertisement"){
           viewHolder.title.text =  dataSet[position].title
           viewHolder.description.text = dataSet[position].description
           viewHolder.cardView.isClickable = false
       }else if (dataSet[position].title != "" && dataSet[position].description != "" ) {
           viewHolder.cardView.isClickable = true
         //   val s1 = "Date: " + dataSet[position].date
            val s2 = "Title: " + dataSet[position].title
            val s3 = "Description: " + dataSet[position].description
         /*   val  s4 = "Duration: " + dataSet[position].duration
            val s5 = "Location: " + dataSet[position].location
            viewHolder.date.text = s1
             viewHolder.duration.text = s4
            viewHolder.location.text = s5*/
            viewHolder.title.text = s2
            viewHolder.description.text = s3

        }
    }
    override fun getItemCount() = dataSet.size


}