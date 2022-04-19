package it.polito.lab3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import it.polito.lab3.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       @Suppress("UNUSED_VARIABLE")
       val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.home_activity)

           //drawerLayout = binding.drawerLayout

           val navController = this.findNavController(R.id.fragmentContainerView2)
      //

   }

}
