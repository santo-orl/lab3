package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.*
import it.polito.lab4.timeSlots.Slot

class Chat{
    var title: String? = null
    var userRec: String? = null
    var slot_id: String? = null
    var userSend: String? = null


    constructor(){}

    constructor(title:String?, userRec:String?,slot_id: String?, userSend: String?){
        this.title = title
        this.userRec = userRec
        this.slot_id = slot_id
        this.userSend = userSend
    }
}
class ChatListFragment: Fragment() {

    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""

    private lateinit var userListRecView: RecyclerView
    private lateinit var userList: ArrayList<Chat>
    private lateinit var userChatAdapter: UserChatAdapter

    private var splitSend:String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userListRecView = view.findViewById(R.id.userRecView)

        vm.email.observe(this.viewLifecycleOwner) { it ->
            id = it
            userList = arrayListOf()
            userChatAdapter = UserChatAdapter(this.requireContext(), userList)

            userListRecView.layoutManager = LinearLayoutManager(this.requireContext())
            userListRecView.adapter = userChatAdapter

            val senderUser = it


            val sx = senderUser.split("@", ".")
            for (s in sx) {
                splitSend += s
            }
            vm.setSlot(Slot("","","","","",-1,"",""))
           var ref =  db.collection("chats").document(id)
                ref.get().addOnSuccessListener{

                if(!it.data.isNullOrEmpty()){
                    Log.i("test chat list", it.data?.size.toString())
                val getUser = it.data as HashMap<*, *>
                for(i in 1..it.data?.size!!){
                    ref.collection(getUser[i.toString()].toString()).get().addOnSuccessListener{ result ->
                        for (document in result) {

                            val getTitle = document.data as HashMap<*, *>
                            Log.i("test chat list", getTitle.toString())
                            userList.add(Chat(getTitle["title"].toString(),getUser[i.toString()].toString(),getTitle["id"].toString(), senderUser ))
                        }
                        userChatAdapter.notifyDataSetChanged()

                    }
                }

                }
                    userChatAdapter.setOnClick(object : ChatUI.ChatListener{
                        override fun onChatClick(position: Int) {
                            vm.setChat(userList[position])
                            Log.i("listChat", "click")
                        }

                    })
            }.addOnFailureListener{e->
                Log.i("test chat list failed", e.toString())
            }




        }

    }
}
