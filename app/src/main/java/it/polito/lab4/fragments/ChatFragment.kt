package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.lab4.R
import it.polito.lab4.ViewModel
import it.polito.lab4.timeSlots.Slot

class ChatFragment: Fragment() {
    private val vm: ViewModel by activityViewModels()
    private lateinit var id: String
    private lateinit var accept_btn: Button
    private lateinit var reject_btn: Button
    private lateinit var slot: Slot

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)
        vm.email.observe(this.viewLifecycleOwner) {
            id = it
            /*if (id != "") {
                //readData(id)
            }*/
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accept_btn = view.findViewById(R.id.accept_btn)
        reject_btn = view.findViewById(R.id.reject_btn)
        vm.slot.observe(this.viewLifecycleOwner){
            slot = it
            Log.i("TESTSLOT",it.toString())
            Log.i("TESTSLOT",id)
            Log.i("TESTSLOT",slot.user)
            if (slot.user !== id){
                //se gli utenti sono diversi
                //mostra i bottoni di accept e reject
                accept_btn.visibility = View.VISIBLE
                accept_btn.isClickable = true
                reject_btn.visibility = View.VISIBLE
                reject_btn.isClickable = true

            }else{
                //gli utenti sono uguali
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