package it.polito.lab4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val context: Context, val userList: ArrayList<User>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.user_chat_list, parent, false)
        return UserAdapter.UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentUser = userList[position]

        if (holder.javaClass == UserAdapter.UserViewHolder::class.java) {
            //do the stuff for sent view holder
            val viewHolder = holder as UserAdapter.UserViewHolder
            holder.user.text = currentUser.name

        }
    }

        override fun getItemCount(): Int {
            return userList.size
        }

        class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val user = itemView.findViewById<TextView>(R.id.userChatList)
        }

    }
