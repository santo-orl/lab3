package it.polito.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.editButton1)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_timeEditFragment_to_timeSlotFragment)
        }
    }


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


