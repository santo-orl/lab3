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
import it.polito.lab4.fragments.TimeSlotOthersListFragment
import it.polito.lab4.skills.Skill
import it.polito.lab4.skills.SkillUI

import kotlinx.android.synthetic.main.fragment_item_list1.view.*



class Adapter_homeFrg(private val dataSet: ArrayList<Skill>) :
    RecyclerView.Adapter<Adapter_homeFrg.ViewHolder>(),SkillUI.SkillSaved {
    constructor(dataSet: ArrayList<Skill>, itemClick: (Int) -> Unit) : this(dataSet)


    private lateinit var skillListener: SkillUI.SkillListener
    fun setOnTodoClick(listener: SkillUI.SkillListener) {
        skillListener = listener
    }

    class ViewHolder(view: View, skillListener: SkillUI.SkillListener,
                     skillSaved: SkillUI.SkillSaved) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title_itemList
        val description: TextView = view.slotDesc
        val user: TextView = view.userItem
        val iconDeleteSlot: ImageView = view.delete_card
        val cardView: CardView = itemView.findViewById(R.id.card_list)

        init {
            /*iconDeleteSlot.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    slotListener.onSlotDeleted(adapterPosition)
                }
            }*/
            cardView.setOnClickListener{
                Log.i("test ","Vede il click???")
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    skillListener.onSkillClick(adapterPosition)
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
        return ViewHolder(view, skillListener,this)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //private val timeSlotViewModel: TimeSlotViewModel by iewModels()
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        if(dataSet[0].title == "No skills available" || dataSet[0].description == "Other users have not yet placed advertisements, be the first, add it in your profile!"
                    || dataSet[0].title == "No skills found" || dataSet[0].description == "No one has the skill you are searching for!") {
            //viewHolder.title.text = dataSet[position].title
                Log.i("no skills", "ENTRAAA")
            viewHolder.title.text = dataSet[position].title
            //viewHolder.description.text = dataSet[position].description
            viewHolder.description.text = dataSet[position].description
            viewHolder.cardView.isClickable = false
            viewHolder.iconDeleteSlot.isClickable = false
            viewHolder.iconDeleteSlot.visibility = View.GONE

            viewHolder.user.visibility = View.GONE

        }else if  (dataSet[position].title == "" && dataSet[position].description == "" ) {
            viewHolder.title.text = "No skills available"
            viewHolder.description.text = "No one has published any skills"
            viewHolder.cardView.isClickable = false
            viewHolder.iconDeleteSlot.isClickable = false
            viewHolder.iconDeleteSlot.visibility = View.GONE

            viewHolder.user.visibility = View.GONE
        }else if (dataSet[position].title != "" && dataSet[position].description != "" ) {
            viewHolder.cardView.isClickable = true
            viewHolder.iconDeleteSlot.isClickable = false
            viewHolder.iconDeleteSlot.visibility = View.GONE
            viewHolder.cardView.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    skillListener.onSkillClick(position)
                    Log.i("test ","Vede il click???!!")
                }

                val activity = it.context as? AppCompatActivity
                activity?.supportFragmentManager?.commit {
                    addToBackStack(TimeSlotOthersListFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<TimeSlotOthersListFragment>(R.id.myNavHostFragment)
                    // Log.i("test2", "Vede il click")
                    //viewHolder.cardView.findNavController().navigate(R.id.action_itemListFragment_to_timeSlotDetailsFragment)
                }
            }
            //   val s1 = "Date: " + dataSet[position].date
           // val s2 = "Title: " + dataSet[position].title
            //val s3 = "Description: " + dataSet[position].description

            viewHolder.title.text = dataSet[position].title
            viewHolder.description.text = dataSet[position].description
            if(dataSet[position].nickname != "Nickname"){
                viewHolder.user.text = dataSet[position].nickname
            }else {
                viewHolder.user.text = dataSet[position].user
            }

        }
    }
    override fun getItemCount() = dataSet.size
    /*override fun onSkillTitleUpdated(position: Int, title: String) {
        dataSet[position].title = title
        // dataSet[position].pos = position+1
    }*/

    /*override fun onSlotDescUpdated(position: Int, description: String) {
        dataSet[position].description = description
    }*/

    override fun onSkillTitleUpdated(position: Int, title: String) {
        TODO("Not yet implemented")
    }

    override fun onSkillDescUpdated(position: Int, description: String) {
        TODO("Not yet implemented")
    }


}