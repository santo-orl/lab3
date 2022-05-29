package it.polito.lab4

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab4.fragments.ChatFragment
import it.polito.lab4.fragments.TimeSlotOthersListFragment
import kotlinx.android.synthetic.main.fragment_item_list.view.*

class UserChatAdapter(val context: Context, val userList: ArrayList<User>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.user_chat_list, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentUser = userList[position]

        if (holder.javaClass == UserChatAdapter.UserViewHolder::class.java) {
            //do the stuff for sent view holder
            val viewHolder = holder as UserChatAdapter.UserViewHolder
            holder.user.text = currentUser.name

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.card_chats)
        val title: TextView = itemView.findViewById<TextView>(R.id.title_slot)
        val user: TextView = itemView.findViewById<TextView>(R.id.slotUser)

        init {
            card.setOnClickListener {
                Log.i("Click chat", "CLICCO SUL NOME")
                }
            }
        }

    }

