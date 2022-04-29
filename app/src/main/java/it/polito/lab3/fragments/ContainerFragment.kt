package it.polito.lab3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.lab3.timeSlots.Adapter_frgTime
import it.polito.lab3.R
import it.polito.lab3.timeSlots.Slot
import kotlinx.android.synthetic.main.activity_edit_profile.*


class ContainerFragment : Fragment() {
    private lateinit var adapterFrgTime: Adapter_frgTime
    private var slotList: ArrayList<Slot> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_container, container, false)

        // Add the following lines to create RecyclerView
        if(slotList.isEmpty()){
            val prova: ArrayList<Slot> = arrayListOf()
            prova.add(Slot("No advertisement", "Click on the button below to add your first advertisement"))
            recycler.layoutManager = LinearLayoutManager(view.context)
            adapterFrgTime = Adapter_frgTime(prova)
            recycler.adapter = adapterFrgTime
        }else{
            recycler.layoutManager = LinearLayoutManager(view.context)
            adapterFrgTime = Adapter_frgTime(slotList)
            recycler.adapter = adapterFrgTime
        }
        return view
    }

}



