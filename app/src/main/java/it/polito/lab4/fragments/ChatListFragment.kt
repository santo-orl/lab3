package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.*
import it.polito.lab4.chat.ChatUI
import it.polito.lab4.chat.UserChatAdapter
import it.polito.lab4.timeSlots.Slot

class Chat{
    var title: String? = null
    var other: String? = null
    var slot_id: String? = null
    var owner: String? = null


    constructor(){}

    constructor(title:String?, other:String?,slot_id: String?, owner: String?){
        this.title = title
        this.other = other
        this.slot_id = slot_id
        this.owner = owner
    }
}
class ChatListFragment: Fragment() {

    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""

    private lateinit var userListRecView: RecyclerView
    private lateinit var chatList: ArrayList<Chat>
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
        activity?.title = "Your messages"

        userListRecView = view.findViewById(R.id.userRecView)

        vm.email.observe(this.viewLifecycleOwner) { it ->
            id = it
            chatList = arrayListOf()
            userChatAdapter = UserChatAdapter(this.requireContext(), chatList)

            userListRecView.layoutManager = LinearLayoutManager(this.requireContext())
            userListRecView.adapter = userChatAdapter


            val sx = it.split("@", ".")
            for (s in sx) {
                splitSend += s
            }
            vm.setSlot(Slot("", "", "", "", "", -1, "", "", -0.1))
            var ref = db.collection("chats").document(id)
            ref.get().addOnSuccessListener {
                Log.i("test chat list","ENTRA?")
                if (!it.data.isNullOrEmpty()) {
                    Log.i("test chat list1", it.data?.size.toString())
                    val getOther = it.data as HashMap<*, *>
                    for (i in 1..it.data?.size!!) {
                        Log.i("test chat list!!", getOther[i.toString()].toString())
                        ref.collection(getOther[i.toString()].toString()).get()
                            //!!!!!!!!!!!!!!!!!!
                            .addOnSuccessListener { result ->
                                Log.i("test chat list55555", result.size().toString())
                                if (result.size() != 0) {
                                    for (document in result) {
                                        val getSlot = document.data as HashMap<*, *>
                                        chatList.add(
                                            Chat(
                                                getSlot["title"].toString(),
                                                getOther[i.toString()].toString(),
                                                getSlot["id"].toString(),
                                                getSlot["user"].toString()
                                            )
                                        )
                                    }
                                } else {
                                    chatList.add(
                                        Chat(
                                            "Start a conversation with someone!",
                                            "No chats", "",
                                            ""
                                        )
                                    )
                                }
                                userChatAdapter.notifyDataSetChanged()
                                Log.i("test chat list", chatList.toString())
                            }
                    }

                }
                userChatAdapter.setOnClick(object : ChatUI.ChatListener {
                    override fun onChatClick(position: Int) {
                        vm.setChat(chatList[position])
                        Log.i("listChat", "click")
                    }

                })
            }.addOnFailureListener { e ->
                Log.i("test chat list failed", e.toString())
            }




        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.commit {
                    addToBackStack(HomeFragment::class.toString())
                    setReorderingAllowed(true)
                    replace<HomeFragment>(R.id.myNavHostFragment)
                }

            }
        })
    }
}
