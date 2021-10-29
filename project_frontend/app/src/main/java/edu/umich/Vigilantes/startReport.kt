package edu.umich.Vigilantes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class startReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.list_report)

        //Find home button
        var homeButton = findViewById<Button>(R.id.homeButton)

        //Listener for home click
        homeButton.setOnClickListener {
            finish()    //End current activity
        }
    }
}