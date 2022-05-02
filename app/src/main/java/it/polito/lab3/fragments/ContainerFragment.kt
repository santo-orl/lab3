package it.polito.lab3.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.polito.lab3.R
import it.polito.lab3.timeSlots.Adapter_frgTime
import it.polito.lab3.timeSlots.Slot


class ContainerFragment : Fragment(R.layout.fragment_container) {
    private lateinit var adapterFrgTime: Adapter_frgTime
    private var slotList: ArrayList<Slot> = arrayListOf()
    private lateinit var add_button: FloatingActionButton
/* override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
): View {
    // Inflate the layout for this fragment
    val view: View = inflater.inflate(R.layout.fragment_container, container, false)

    // Add the following lines to create RecyclerView
    if(slotList.isEmpty(
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
}*/

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    add_button = view.findViewById(R.id.add_FAB)
    add_button.setOnClickListener{
      findNavController().navigate(R.id.action_containerFragment_to_timeSlotEditFragment)
    }
}

}



