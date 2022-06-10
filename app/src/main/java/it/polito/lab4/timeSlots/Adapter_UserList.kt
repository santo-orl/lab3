package it.polito.lab4.timeSlots

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab4.R
import it.polito.lab4.fragments.TimeSlotDetailsFragment

import kotlinx.android.synthetic.main.fragment_item_list1.view.*



class Adapter_UserList (private val dataSet: ArrayList<Slot>) :
RecyclerView.Adapter<Adapter_UserList.ViewHolder>(),SlotUI.SlotSaved {
    constructor(dataSet: ArrayList<Slot>, itemClick: (Int) -> Unit) : this(dataSet)


    private lateinit var slotListener: SlotUI.SlotListener
    fun setOnTodoDeleteClick(listener: SlotUI.SlotListener) {
        slotListener = listener
    }



    class ViewHolder(view: View, slotListener: SlotUI.SlotListener,
                     slotSaved: SlotUI.SlotSaved) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title_itemList
        val description: TextView = view.slotDesc
        val user: TextView = view.userItem
        val iconDeleteSlot: ImageView = view.delete_card
        val cardView: CardView = itemView.findViewById(R.id.card_list)

        init {
            iconDeleteSlot.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    slotListener.onSlotDeleted(adapterPosition)
                }
            }
            cardView.setOnClickListener{
                Log.i("test ","Vede il click???")
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    slotListener.onSlotClick(adapterPosition)
                }
            }
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
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_item_list1, viewGroup, false)
        return ViewHolder(view, slotListener,this)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //private val timeSlotViewModel: TimeSlotViewModel by iewModels()
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

       if((dataSet[0].title == "No advertisement" && dataSet[0].description == "Click on the button below to add your first advertisement")
           || (dataSet[0].title == "No advertisement" && dataSet[0].description == "No advertisements for this skill")) {
           viewHolder.title.text = dataSet[position].title
           viewHolder.description.text = dataSet[position].description
           viewHolder.cardView.isClickable = false
           viewHolder.iconDeleteSlot.isClickable = false
           viewHolder.iconDeleteSlot.visibility = View.GONE

           viewHolder.user.visibility = View.GONE

       }else if  (dataSet[position].title == "" && dataSet[position].description == "" ) {
           viewHolder.title.text = "No advertisement"
           viewHolder.description.text = "Click on the button below to add your first advertisement"
           viewHolder.cardView.isClickable = false
           viewHolder.iconDeleteSlot.isClickable = false
           viewHolder.iconDeleteSlot.visibility = View.GONE

           viewHolder.user.visibility = View.GONE
       }else if (dataSet[position].title != "" && dataSet[position].description != "" ) {
           viewHolder.cardView.isClickable = true
           viewHolder.cardView.setOnClickListener {
               if (position != RecyclerView.NO_POSITION) {
                   slotListener.onSlotClick(position)
                   //Log.i("test ","Vede il click???!!")
               }
               viewHolder.iconDeleteSlot.setOnClickListener {
                   if (position != RecyclerView.NO_POSITION) {
                       slotListener.onSlotDeleted(position)
                       //Log.i("test ","Vede il click???!!")
                   }
               }
               val activity = it.context as? AppCompatActivity
               activity?.supportFragmentManager?.commit {
                   addToBackStack(TimeSlotDetailsFragment::class.toString())
                   setReorderingAllowed(true)
                   replace<TimeSlotDetailsFragment>(R.id.myNavHostFragment)
                  // Log.i("test2", "Vede il click")
                    //viewHolder.cardView.findNavController().navigate(R.id.action_itemListFragment_to_timeSlotDetailsFragment)
               }
           }
         //   val s1 = "Date: " + dataSet[position].date
            val s2 = "Title: " + dataSet[position].title
            val s3 = "Description: " + dataSet[position].description
            viewHolder.title.text = s2
            viewHolder.description.text = s3
           viewHolder.user.text = dataSet[position].user

        }
    }
    override fun getItemCount() = dataSet.size
    override fun onSlotTitleUpdated(position: Int, title: String) {
        dataSet[position].title = title
       // dataSet[position].pos = position+1
    }

    override fun onSlotDescUpdated(position: Int, description: String) {
        dataSet[position].description = description
    }


}