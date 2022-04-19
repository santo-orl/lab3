package it.polito.lab3

import it.polito.lab3.DatePickerFragment
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import it.polito.lab3.databinding.FragmentTimeSlotEditBinding
import kotlinx.android.synthetic.main.fragment_time_slot_edit.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TimeSlotEditFragment : Fragment() {
    private var _binding: FragmentTimeSlotEditBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTimeSlotEditBinding.bind(view)

        binding.apply {
            date_edit.setOnClickListener {
                // create new instance of DatePickerFragment
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        date_edit.setText(date)
                    }
                }

                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
            from_edit.setOnClickListener {
                // create new instance of DatePickerFragment
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val duration = bundle.getString("SELECTED_TIME")
                        from_edit.setText(duration)
                    }
                }

                // show
                timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
            }

            to_edit.setOnClickListener {
                // create new instance of DatePickerFragment
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val duration = bundle.getString("SELECTED_TIME")
                        to_edit.setText(duration)
                    }
                }

                // show
                timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_slot_edit, container, false)
    }



}