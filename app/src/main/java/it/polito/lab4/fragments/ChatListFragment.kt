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

class ChatListFragment: Fragment() {

    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""

    private lateinit var userListRecView: RecyclerView
    private lateinit var userList: ArrayList<User>
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

        vm.email.observe(this.viewLifecycleOwner) {
            id = it

            userList = ArrayList()
            userChatAdapter = UserChatAdapter(this.requireContext(), userList)

            userListRecView.layoutManager = LinearLayoutManager(this.requireContext())
            userListRecView.adapter = userChatAdapter

            val senderUser = it


            val sx = senderUser.split("@", ".")
            for (s in sx) {
                splitSend += s
            }


            //db.collection("chats").document().collection("messages")


            db.collection("users").get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        if (document.id != id) {
                            val u = document.data as HashMap<*, *>
                            var user = User(
                                u["name"].toString(),
                                u["nickname"].toString(),
                                u["email"].toString(),
                                u["location"].toString(),
                                "",
                                0
                            )
                            Log.i("chatuser",user.toString())
                            userList.add(user)
                            userChatAdapter.notifyDataSetChanged()
                        }

                    }
                }

            /*db.collection("chats").document().collection("messages")
                .addSnapshotListener { snapshot, result ->
                    for (document in snapshot.documents) {
                        if (document.id != id) {
                            val u = document.data as HashMap<*, *>
                            var user = User(
                                u["name"].toString(),
                                u["nickname"].toString(),
                                u["email"].toString(),
                                u["location"].toString(),
                                "",
                                0
                            )

                            userList.add(user)
                        }

                    }
                }*/

        }

    }
}
