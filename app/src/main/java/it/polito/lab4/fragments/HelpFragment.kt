package it.polito.lab4.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import it.polito.lab4.R
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class HelpFragment : Fragment(R.layout.fragment_help) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_help, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = "Help"

        var profile_button = view.findViewById<Button>(R.id.buttonProfile)
        var slot_button = view.findViewById<Button>(R.id.buttonSlot)
        var home_button = view.findViewById<Button>(R.id.buttonHome)
        var chat_button = view.findViewById<Button>(R.id.buttonChat)
        var done_button = view.findViewById<Button>(R.id.buttonJobDone)



        profile_button.setOnClickListener {
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(ShowProfileFragment::class.toString())
                setReorderingAllowed(true)
                replace<ShowProfileFragment>(R.id.myNavHostFragment)
            }
        }
        slot_button.setOnClickListener {
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(ListSkillUserFragment::class.toString())
                setReorderingAllowed(true)
                replace<ListSkillUserFragment>(R.id.myNavHostFragment)
            }
        }
        home_button.setOnClickListener {
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(HomeFragment::class.toString())
                setReorderingAllowed(true)
                replace<HomeFragment>(R.id.myNavHostFragment)
            }
        }
        chat_button.setOnClickListener {
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(ChatListFragment::class.toString())
                setReorderingAllowed(true)
                replace<ChatListFragment>(R.id.myNavHostFragment)
            }
        }
        done_button.setOnClickListener {
            val activity = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack(AssignedAcceptedFragment::class.toString())
                setReorderingAllowed(true)
                replace<AssignedAcceptedFragment>(R.id.myNavHostFragment)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


}