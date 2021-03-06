package it.polito.lab4.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import it.polito.lab4.AAslot
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.chat.Message
import it.polito.lab4.chat.MessageAdapter
import it.polito.lab4.reviews.Review
import it.polito.lab4.timeSlots.Slot
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment: Fragment() {
    private val vm: ViewModel by activityViewModels()
    private var senderUser: String = ""
    private var receiverUser: String = ""
    private lateinit var accept_btn: Button
    private lateinit var reject_btn: Button
    private var slot= Slot("", "", "", "", "", -1, "", "", -0.1)
    private var slot_id = ""

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var layout_messageArea: LinearLayout
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var lineView: View

    private lateinit var rateText: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var optComment_field: EditText
    private lateinit var saveRating_btn: Button

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

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        chatRecyclerView = requireView().findViewById(R.id.chatRecyclerView)
        messageBox = requireView().findViewById(R.id.messageBox)
        sendButton = requireView().findViewById(R.id.sendButton)
        layout_messageArea = requireView().findViewById(R.id.layout_messageArea)
        lineView = requireView().findViewById(R.id.view)

        accept_btn = view.findViewById(R.id.accept_btn)
        reject_btn = view.findViewById(R.id.reject_btn)

        rateText = requireView().findViewById(R.id.rateText)
        ratingBar = requireView().findViewById(R.id.chatRatingBar)
        optComment_field = requireView().findViewById(R.id.optComment)
        saveRating_btn = requireView().findViewById(R.id.saveRating_btn)

        rateText.visibility = View.GONE
        ratingBar.visibility = View.GONE
        ratingBar.isClickable = false
        optComment_field.visibility = View.GONE
        optComment_field.isClickable = false
        saveRating_btn.visibility = View.GONE
        saveRating_btn.isClickable = false


        vm.email.observe(this.viewLifecycleOwner) { it ->
            senderUser = it

            val sx = senderUser.split("@", ".")
            for (s in sx) {
                splitSend += s
            }


            vm.slot.observe(this.viewLifecycleOwner) { it ->

                if (it.title != "") {
                    Log.i("Test_chat", "entra con slot")
                    slot = it
                    slot_id = slot.id
                    receiverUser = slot.user

                    activity?.title = it.title
                    Log.i("recuser",receiverUser)
                    readData(receiverUser, slot_id)

                    if(slot.user == senderUser){
                        accept_btn.visibility = View.VISIBLE
                        accept_btn.isClickable = true
                        reject_btn.visibility = View.VISIBLE
                        reject_btn.isClickable = true
                    }else {
                        //appUser sta richiedendo lo slot di otherUser
                        //nascondi bottoni per accept e reject
                        accept_btn.visibility = View.GONE
                        accept_btn.isClickable = false
                        reject_btn.visibility = View.GONE
                        reject_btn.isClickable = false
                    }
                } else {
                    Log.i("Test_chat", "entra con chat")
                    vm.chat.observe(this.viewLifecycleOwner) { ref ->
                        slot_id = ref.slot_id.toString()
                        receiverUser = ref.other.toString()
                        var sendUser = ref.owner.toString()
                        Log.i("TEST CHAT","$receiverUser $sendUser    $senderUser")
                        readData(receiverUser, slot_id)

                        if(sendUser == senderUser){
                            accept_btn.visibility = View.VISIBLE
                            accept_btn.isClickable = true
                            reject_btn.visibility = View.VISIBLE
                            reject_btn.isClickable = true
                        }else {
                            //appUser sta richiedendo lo slot di otherUser
                            //nascondi bottoni per accept e reject
                            accept_btn.visibility = View.GONE
                            accept_btn.isClickable = false
                            reject_btn.visibility = View.GONE
                            reject_btn.isClickable = false
                        }

                        activity?.title = ref.title
                        Log.i("recuser",receiverUser)
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
                  sendMessage("NORMAL")
                }

                accept_btn.setOnClickListener {


                    db.collection("users").document(receiverUser).get()
                        .addOnSuccessListener { rec ->
                            db.collection("slots").document(slot_id).get()
                                .addOnSuccessListener { slot ->
                                    if (rec.get("hours").toString().toDouble() >= slot.get("hours")
                                            .toString().toDouble()
                                    ) {
                                        db.collection("slots").document(slot_id).get().addOnSuccessListener {
                                            var aaslot = AAslot(it.get("title").toString(), slot_id, senderUser,receiverUser)
                                            db.collection("assigned_accepted_slot").document(slot_id).set(aaslot)
                                        }
                                        Log.i("Test pagamento", "entra")
                                        val cost =  rec.get("hours").toString().toDouble()- slot.get("hours")
                                            .toString().toDouble()

                                        Log.i("Test pagamento", "costo: $cost")
                                        var map: MutableMap<String, String> = HashMap()
                                        map["status"] = "Sold"
                                        db.collection("slots").document(slot_id)
                                            .set(map, SetOptions.merge())
                                        map = HashMap()
                                        map["hours"] = cost.toString()
                                        //togli il costo da uno
                                        db.collection("users").document(receiverUser)
                                            .set(map, SetOptions.merge())
                                        //aggiungi pagamento all'altro
                                        var updateCost = 0.0
                                        db.collection("users").document(senderUser).get()
                                            .addOnSuccessListener { sen ->
                                                updateCost = sen.get("hours").toString().toDouble()
                                                Log.i("Test pagamento", "costo: $updateCost")
                                                map = HashMap()
                                                updateCost += cost
                                                Log.i("Test pagamento", "costo: $updateCost")
                                                map["hours"] = updateCost.toString()
                                                db.collection("users").document(senderUser)
                                                    .set(map, SetOptions.merge())
                                            }
                                        sendMessage("ACCEPTED")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "The user does not have enough time to pay you",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.i("Test pagamento", "NON ENTRA")
                                        sendMessage("ERROR")
                                    }
                                }

                        }
                }


                }

                reject_btn.setOnClickListener {
                    //torna indietro senza cambiare lo stato dello slot
                    //cancella la chat relativa allo slot
                    sendMessage("REJECTED")
                    activity?.supportFragmentManager?.commit {
                        addToBackStack(ChatListFragment::class.toString())
                        setReorderingAllowed(true)
                        replace<ChatListFragment>(R.id.myNavHostFragment)
                    }
                    Toast.makeText(this.context,"Request rejected",Toast.LENGTH_SHORT).show()

                }

            }




    private fun sendMessage(b: String) {
        var message = ""

        if (b.equals("NORMAL")) {
            message = messageBox.text.toString()
        }else if(b.equals("ERROR")){
            message = "YOUR REQUEST HAS BEEN REJECTED BECAUSE YOU DON'T HAVE ENOUGH TIME TO SPEND IN THIS SLOT"
        }else if(b.equals("REJECTED")){
            message = "YOUR REQUEST HAS BEEN REJECTED FROM THE OWNER"
        }
        else{
            message = "YOUR REQUEST HAS BEEN ACCEPTED"
        }
        val timeZone = TimeZone.getTimeZone("UTC")
        val date = Calendar.getInstance(timeZone).time
        Log.i("message info", date.toString())
        //val date = Calendar.getInstance().time
        val  DATE_FORMAT =  SimpleDateFormat("dd/MM/yy HH:mm:ss");
        var sentTime = DATE_FORMAT.format(date)
        Log.i("message info", sentTime)
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
            if(slot.id!=""){
                ref2.set(slot)
            }
            ref2.collection("messages").document().set(messageObject)
                .addOnSuccessListener {
                    Log.i("testChat", "Entra")
                }
        }


        var ref3 = db.collection("chats").document(receiverUser)
        ref3.get().addOnSuccessListener {
            var map = it.data.orEmpty().toMutableMap()
            if (!map.values.contains(senderUser)) {
                map[(map.size + 1).toString()] = senderUser
                Log.i("testChat", map.toString())
                ref3.set(map, SetOptions.merge())
            }
            var ref4 = ref3.collection(senderUser).document(slot_id)
            if(slot.id!=""){
                ref4.set(slot)
            }

            ref4.collection("messages").document().set(messageObject)
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
                        // Log.i("message", message.toString())

                        messageList.add(message)
                        ratingFun(message)
                    }

                    messageAdapter.notifyDataSetChanged()
                }
            }
        messageBox.setText("")
    }

    private fun ratingFun(message: Message) {
        if (message.message.equals("YOUR REQUEST HAS BEEN ACCEPTED")) {
            //fai vedere barra di rating e editText per commento opzionale
            accept_btn.visibility = View.GONE
            accept_btn.isClickable = false
            reject_btn.visibility = View.GONE
            reject_btn.isClickable = false
            chatRecyclerView.visibility = View.GONE
            layout_messageArea.visibility = View.GONE
            lineView.visibility = View.GONE
            messageBox.visibility = View.GONE
            messageBox.isClickable = false
            sendButton.visibility = View.GONE
            sendButton.isClickable = false

            rateText.visibility = View.VISIBLE
            ratingBar.visibility = View.VISIBLE
            ratingBar.isClickable = true
            optComment_field.visibility = View.VISIBLE
            optComment_field.isClickable = true
            saveRating_btn.visibility = View.VISIBLE
            saveRating_btn.isClickable = true

            saveRating_btn.setOnClickListener {
                //salva rating e commento
                var rating = ratingBar.rating //float
                if (rating != 0.0F) {
                    var optComment = optComment_field.text.toString()
                    var review =
                        Review(senderUser, receiverUser, rating, optComment)
                    db.collection("users").document(receiverUser)
                        .collection("reviews").document().set(review)
                    db.collection("chats").document(senderUser).collection(receiverUser)
                        .document(slot_id).delete()

                    //ritorna alla lista delle chat
                    activity?.supportFragmentManager?.commit {
                        addToBackStack(ChatListFragment::class.toString())
                        setReorderingAllowed(true)
                        replace<ChatListFragment>(R.id.myNavHostFragment)
                    }
                } else {
                    Toast.makeText(
                        this.context,
                        "You have to add a review",
                        Toast.LENGTH_LONG
                    )
                }
            }
        }
    }


    private fun readData(receiverUser: String, slot_id : String) {

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this.requireContext(), messageList, senderUser)

        chatRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        chatRecyclerView.adapter = messageAdapter

        db.collection("chats").document(senderUser).collection(receiverUser)
            .document(slot_id)
            .collection("messages")
            .orderBy("sentTime")
            .get().addOnSuccessListener { snapshot ->
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
                      //  Log.i("message", "${message.message.toString()}   ${message.sentTime.toString()}")

                        messageList.add(message)
                        ratingFun(message)
                    }
                    //messageList.sortBy { it.sentTime }
                    messageAdapter.notifyDataSetChanged()
                }
            }
        messageBox.setText("")


    }

    //creo la pencil icon in alto a dx
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId.equals(R.id.slotDetails)) {
            Log.i("ENTRAMENU", "on create options menu")

            vm.slot.observe(this.viewLifecycleOwner) { it ->
                if (it.title == "") {
                    db.collection("slots").document(slot_id).get()
                        .addOnSuccessListener { s ->
                            var add= Slot(
                                s["title"].toString(),
                                s["description"].toString(),
                                s["date"].toString(),
                                s["duration"].toString(),
                                s["location"].toString(),
                                s["pos"].toString().toInt(),
                                s["user"].toString(),
                                s["status"].toString(),
                                s["hours"].toString().toDouble()
                            )
                            add.reference(slot_id)
                            vm.setSlot(add)
                        }
                }
                }
            activity?.supportFragmentManager?.commit {
                addToBackStack(TimeSlotDetailsFragment::class.toString())
                setReorderingAllowed(true)
                replace<TimeSlotDetailsFragment>(R.id.myNavHostFragment)
            }
            // findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment)
            return true
        }
        return false
    }

}
