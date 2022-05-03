package it.polito.lab3.timeSlots

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab3.R
import it.polito.lab3.TimeSlotViewModel
import it.polito.lab3.fragments.TimeSlotDetailsFragment
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import kotlinx.android.synthetic.main.fragment_time_slot_details.view.*


class Adapter_frgTime (private val dataSet: ArrayList<Slot>) :
RecyclerView.Adapter<Adapter_frgTime.ViewHolder>() {
    constructor(dataSet: ArrayList<Slot>, itemClick: (Int) -> Unit) : this(dataSet)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title_itemList
        val description:  TextView = view.description_itemList
        val cardView: CardView =  itemView.findViewById(R.id.card_list)
       }

      /*   fun bind(listener: (Int) -> Unit)= with(itemView){
            setOnClickListener { listener(layoutPosition) }
        val title: TextView = view.title_itemList
        val description:  TextView = view.description_itemList
        val cardView: CardView =  itemView.findViewById(R.id.card_list)
        init {
            itemView.setOnClickListener ( {itemClick(layoutPosition)})

                // Log.i("test2","Vede il click")
               // cardView.findNavController().navigate(R.id.action_itemListFragment_to_timeSlotDetailsFragment)
        }*/

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

        //private val timeSlotViewModel: TimeSlotViewModel by iewModels()
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

       if(dataSet[0].title == "No advertisement" && dataSet[0].description == "Click on the button below to add your first advertisement"){
           viewHolder.title.text =  dataSet[position].title
           viewHolder.description.text = dataSet[position].description
           viewHolder.cardView.isClickable = false
       }else if (dataSet[position].title != "" && dataSet[position].description != "" ) {
           viewHolder.cardView.isClickable = true
           viewHolder.cardView.setOnClickListener {
               Log.i("TEST", position.toString())

               val fragment = TimeSlotDetailsFragment.newInstance(position)

               val activity = it.context as? AppCompatActivity
               activity?.supportFragmentManager?.commit {
                   addToBackStack(fragment::class.toString())
                   setReorderingAllowed(true)
                   replace(R.id.myNavHostFragment, fragment)
                   Log.i("test2", "Vede il click")
                    //viewHolder.cardView.findNavController().navigate(R.id.action_itemListFragment_to_timeSlotDetailsFragment)
               }
           }
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