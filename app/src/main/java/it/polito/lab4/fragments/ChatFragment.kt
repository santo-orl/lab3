package it.polito.lab4.fragments

import android.Manifest.permission_group.CALENDAR
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import it.polito.lab4.Message
import it.polito.lab4.MessageAdapter
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Slot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment: Fragment() {
    private val vm: ViewModel by activityViewModels()
    private var senderUser: String = ""
    private var receiverUser: String = ""
    private lateinit var accept_btn: Button
    private lateinit var reject_btn: Button
    private lateinit var slot: Slot
    private var slot_id = ""

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private val db = FirebaseFirestore.getInstance()

    //to create a unique room of messages between the users of the chat
    var splitSend = ""
    var splitRec = ""
    private lateinit var receiverRoom: String
    private lateinit var senderRoom: String
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')


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
        chatRecyclerView = requireView().findViewById(R.id.chatRecyclerView)
        messageBox = requireView().findViewById(R.id.messageBox)
        sendButton = requireView().findViewById(R.id.sendButton)

        vm.email.observe(this.viewLifecycleOwner) { it ->
            senderUser = it

            val sx = senderUser.split("@", ".")
            for (s in sx) {
                splitSend += s
            }

            vm.slot.observe(this.viewLifecycleOwner) { it ->
                if (it.title != "") {
                    slot = it
                    slot_id = slot.id
                    receiverUser = slot.user

                    val rx = receiverUser.split("@", ".")
                    for (s in rx) {
                        splitRec += s
                    }
                    readData(receiverUser, slot_id)
                    activity?.title = receiverUser
                } else {
                    vm.chat.observe(this.viewLifecycleOwner) { ref ->
                        slot_id = ref.slot_id.toString()
                        receiverUser = ref.user.toString()
                        readData(receiverUser, slot_id)
                    }
                }
            }
                Log.i("Sender user", senderUser)

                mDbRef = FirebaseDatabase.getInstance().reference
                /*    receiverRoom = splitSend + splitRec + slot.id
            Log.i("Receiver Room", receiverRoom)
            senderRoom = splitRec + splitSend + slot.id
            Log.i("Sender Room", senderRoom)
*/
                //adding the messsage to the db
                sendButton.setOnClickListener {
                    //send the message to the db and from the db the message will be rx by the other user
                    val message = messageBox.text.toString()
                    val date = Calendar.getInstance().time
                    val formatter =
                        SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                    var sentTime = formatter.format(date)
                    val messageObject = Message(message, senderUser, receiverUser, sentTime)

                    var ref = db.collection("chats").document(senderUser)
                    ref.get().addOnSuccessListener {
                        var map = it.data.orEmpty().toMutableMap()
                        if (!map.values.contains(receiverUser)) {
                            map[(map.size + 1).toString()] = receiverUser
                            Log.i("testChat", map.toString())
                            ref.set(map, SetOptions.merge())
                        }
                        var ref2 = ref.collection(receiverUser).document(slot_id)
                        ref2.set(slot)
                        ref2.collection("messages").document().set(messageObject)
                            .addOnSuccessListener {
                                Log.i("testChat", "Entra")
                            }
                    }


                    ref = db.collection("chats").document(receiverUser)
                    ref.get().addOnSuccessListener {
                        var map = it.data.orEmpty().toMutableMap()
                        if (!map.values.contains(senderUser)) {
                            map[(map.size + 1).toString()] = senderUser
                            Log.i("testChat", map.toString())
                            ref.set(map, SetOptions.merge())
                        }
                        var ref2 = ref.collection(senderUser).document(slot_id)
                        ref2.set(slot)
                        ref2.collection("messages").document().set(messageObject)
                            .addOnSuccessListener {
                                Log.i("testChat", "Entra")
                            }
                    }
                    //create a unique door every time this push() is called
                    /*mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }*/
                    db.collection("chats").document(senderUser).collection(receiverUser)
                        .document(slot_id)
                        .collection("messages")
                        .orderBy("sentTime")
                        .addSnapshotListener { snapshot, e ->
                            if (snapshot != null) {
                                Log.i("snapshotsb", "Current data: ${snapshot.documents}$")
                                messageList.clear()
                                for (doc in snapshot.documents) { // doc is a message
                                    val m = doc.data as HashMap<*, *>
                                    var message = Message(
                                        m["message"].toString(),
                                        m["senderId"].toString(),
                                        m["receiverId"].toString(),
                                        m["sentTime"].toString()
                                    )
                                    Log.i("message", message.toString())

                                    messageList.add(message)
                                }
                                messageAdapter.notifyDataSetChanged()
                            }
                        }
                    messageBox.setText("")

                }


                accept_btn = view.findViewById(R.id.accept_btn)
                reject_btn = view.findViewById(R.id.reject_btn)
                vm.slot.observe(this.viewLifecycleOwner) {
                    slot = it
                    Log.i("TESTSLOT", it.toString())
                    Log.i("TESTSLOT", senderUser)
                    Log.i("TESTSLOT", slot.user)
                    if (slot.user == senderUser) {
                        //otherUser sta richiedendo lo slot ad appUser
                        //appUser può accettare o rifiutare
                        //mostra i bottoni di accept e reject
                        accept_btn.visibility = View.VISIBLE
                        accept_btn.isClickable = true
                        reject_btn.visibility = View.VISIBLE
                        reject_btn.isClickable = true

                    } else {
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
                    //cancella la chat relativa allo slot
                }

                reject_btn.setOnClickListener {
                    //torna indietro senza cambiare lo stato dello slot
                    //cancella la chat relativa allo slot
                }

            }
        }

    private fun readData(receiverUser: String, slot_id : String) {

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this.requireContext(), messageList, senderUser)

        chatRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        chatRecyclerView.adapter = messageAdapter

        Log.i("test chat", receiverUser +" "+ slot_id,)
        db.collection("chats").document(senderUser).collection(receiverUser)
            .document(slot_id)
            .collection("messages")
            .orderBy("sentTime").get().addOnSuccessListener{ snapshot->
                if (snapshot != null) {
                    Log.i("TEST", "Current data: ${snapshot.documents}$")
                    messageList.clear()
                    for (doc in snapshot.documents) { // doc is a message
                        val m = doc.data as HashMap<*, *>
                        var message = Message(
                            m["message"].toString(),
                            m["senderId"].toString(),
                            m["receiverId"].toString(),
                            m["sentTime"].toString()
                        )
                        Log.i("test message", message.toString())

                        messageList.add(message)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }
      /* db.collection("chats").document(senderUser).collection(receiverUser)
            .document(slot_id)
            .collection("messages")
            .orderBy("sentTime")
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    Log.i("snapshotsb", "Current data: ${snapshot.documents}$")
                    messageList.clear()
                    for (doc in snapshot.documents) { // doc is a message
                        val m = doc.data as HashMap<*, *>
                        var message = Message(
                            m["message"].toString(),
                            m["senderId"].toString(),
                            m["receiverId"].toString(),
                            m["sentTime"].toString()
                        )
                        Log.i("message", message.toString())

                        messageList.add(message)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }*/
    }
}
