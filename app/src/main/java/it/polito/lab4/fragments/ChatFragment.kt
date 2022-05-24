package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import it.polito.lab4.Message
import it.polito.lab4.MessageAdapter
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.timeSlots.Slot

class ChatFragment: Fragment() {
    private val vm: ViewModel by activityViewModels()
    private var senderUser: String = ""
    private var receiverUser: String = ""
    private lateinit var accept_btn: Button
    private lateinit var reject_btn: Button
    private lateinit var slot: Slot

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    //to create a unique room of messages between the users of the chat
    var splitSend = ""
    var splitRec = ""
    private lateinit var receiverRoom: String
    private lateinit var senderRoom: String
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        vm.email.observe(this.viewLifecycleOwner) {
            senderUser = it

            val sx = senderUser.split("@",".")
            for (s in sx){
                splitSend += s
            }
            Log.i("Sender user",senderUser)
            Log.i("Sender split", splitSend)
        }

        vm.slot.observe(this.viewLifecycleOwner){
            slot = it
            receiverUser = slot.user

            val rx = receiverUser.split("@",".")
            for (s in rx){
                splitRec += s
            }
            Log.i("Receiver user",receiverUser)
            Log.i("Receiver split", splitRec)
            activity?.title = receiverUser
        }

        mDbRef = FirebaseDatabase.getInstance().reference

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        Log.i("Sender uid", senderUid.toString())

        val randomString = (1..senderUid.toString().length)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        val rx = senderUser.split("@").joinToString()
        Log.i("rx", rx)
        Log.i("sent", senderUser)
        //receiverRoom = senderUser.plus(receiverUser)
        receiverRoom = senderUid.toString() + randomString
        //receiverRoom = splitSend + splitRec
        Log.i("Receiver Room", receiverRoom.toString())
        //senderRoom = receiverUser.plus(senderUser)
        senderRoom = randomString + senderUid.toString()
        //senderRoom = splitRec + splitSend
        Log.i("Sender Room", senderRoom.toString())

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        messageBox = view.findViewById(R.id.messageBox)
        sendButton = view.findViewById(R.id.sendButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this.requireContext(),messageList, senderUser)

        chatRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        chatRecyclerView.adapter = messageAdapter


        //logic for adding data to recycler view
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) { //called when there is some change in the db

                    //clear the previous values
                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        //adding the messsage to the db
        sendButton.setOnClickListener {
            //send the message to the db and from the db the message will be rx by the other user
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUser)

            //create a unique door every time this push() is called
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            messageBox.setText("")

        }


        accept_btn = view.findViewById(R.id.accept_btn)
        reject_btn = view.findViewById(R.id.reject_btn)
        vm.slot.observe(this.viewLifecycleOwner){
            slot = it
            Log.i("TESTSLOT",it.toString())
            Log.i("TESTSLOT",senderUser)
            Log.i("TESTSLOT",slot.user)
            if (slot.user == senderUser){
                //otherUser sta richiedendo lo slot ad appUser
                //appUser può accettare o rifiutare
                //mostra i bottoni di accept e reject
                accept_btn.visibility = View.VISIBLE
                accept_btn.isClickable = true
                reject_btn.visibility = View.VISIBLE
                reject_btn.isClickable = true

            }else{
                //appUser sta richiedendo lo slot di otherUser
                //nascondi bottoni per accept e reject
                accept_btn.visibility = View.GONE
                accept_btn.isClickable = false
                reject_btn.visibility = View.GONE
                reject_btn.isClickable = false
            }
        }

        accept_btn.setOnClickListener {
            
            //rendi lo slot non available
            //sposta i soldi da un utente all'altro
        }

        reject_btn.setOnClickListener {
            //torna indietro senza cambiare lo stato dello slot
        }

    }


}