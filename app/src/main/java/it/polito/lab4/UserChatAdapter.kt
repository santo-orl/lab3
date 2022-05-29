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
import it.polito.lab4.fragments.*
import it.polito.lab4.timeSlots.SlotUI
import kotlinx.android.synthetic.main.fragment_item_list.view.*

class UserChatAdapter(val context: Context, val userList: ArrayList<objList>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var chatListener: ChatUI.ChatListener

    fun setOnClick(listener: ChatUI.ChatListener) {
        chatListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.user_chat_list, parent, false)
        return UserViewHolder(view, chatListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentUser = userList[position]
        Log.i("testAdapter", currentUser.toString())

        if (holder.javaClass == UserChatAdapter.UserViewHolder::class.java) {
            //do the stuff for sent view holder
            val viewHolder = holder as UserChatAdapter.UserViewHolder
            holder.user.text = currentUser.user
            holder.title.text = currentUser.title

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }



    class UserViewHolder(itemView: View, chatListener: ChatUI.ChatListener) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.card_chats)
        val title: TextView = itemView.findViewById<TextView>(R.id.title_slot)
        val user: TextView = itemView.findViewById<TextView>(R.id.slotUser)

        init {
            card.setOnClickListener {
                Log.i("Click chat", "CLICCO SUL NOME")
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    chatListener.onChatClick(adapterPosition)
                }
                val activity = it.context as? AppCompatActivity
                activity?.supportFragmentManager?.commit {
                    addToBackStack(ShowProfileFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<ChatFragment>(R.id.myNavHostFragment)
                }
                }
            }
        }

    }

