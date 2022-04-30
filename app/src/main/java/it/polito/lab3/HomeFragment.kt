package it.polito.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView


class HomeFragment : Fragment(R.layout.fragment_home) {
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nv: NavigationView = view.findViewById<NavigationView?>(R.id.nav_view)
        nv.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    // DEVO APRIRE IL FRAMMENTO CHE MOSTRA LO SHOW PROFILE FRAGMENT
                    findNavController().navigate(R.id.action_homeFragment_to_showProfileFragment2)
                    true
                }
                else -> false
            }
        }

    }*/
}