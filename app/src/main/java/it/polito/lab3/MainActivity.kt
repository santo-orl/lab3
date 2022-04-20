package it.polito.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
 // Main activity, is the base for all the fragments
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}