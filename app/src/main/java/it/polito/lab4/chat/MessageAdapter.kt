package it.polito.lab4.chat

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab4.R


class MessageAdapter(val context: Context, val messageList: ArrayList<Message>, val appUser: String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    val ITEM_ERROR = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==ITEM_SENT){
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }else if(viewType==ITEM_RECEIVE){
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }else if(viewType==ITEM_ERROR){
            val view: View = LayoutInflater.from(context).inflate(R.layout.error, parent, false)
            return SentViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.default_message, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) { //current ViewHolder is SentViewHolder
            //do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
            Log.i("sent time", currentMessage.sentTime.toString())

            holder.sentTime.text = currentMessage.sentTime?.dropLast(3)
        }else{
            //do stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
            holder.sentTime.text = currentMessage.sentTime?.dropLast(3)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        Log.i("Current sender Id", currentMessage.senderId.toString())
        Log.i("App user", appUser)
        //if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
        if (currentMessage.message.equals("YOUR REQUEST HAS BEEN REJECTED BECAUSE YOU DON'T HAVE ENOUGH TIME TO SPEND IN THIS SLOT")){
            return ITEM_ERROR
        }else if (currentMessage.message.equals("YOUR REQUEST HAS BEEN REJECTED FROM THE OWNER")) {
            return 4
        }else{
        if(currentMessage.senderId.toString() == appUser){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }}
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    //We need 2 view holders, one for sending the message and one for rx
    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
        val sentTime = itemView.findViewById<TextView>(R.id.sentTime)
    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
        val sentTime = itemView.findViewById<TextView>(R.id.sentTime)
    }

}
