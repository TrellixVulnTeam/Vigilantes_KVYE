package edu.umich.Vigilantes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class addWitnessForm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addwitness_form)

        //Retrieve witness info if witness is existing
        val retrWitness = intent.getParcelableExtra<WitnessInfo>("Existing Witness")
        var position = 0
        //If witness is pre-existing
        if (retrWitness != null) {
            retrWitness.name?.let { Log.d("Witness being edited", it) }
            val nameEntry = findViewById<EditText>(R.id.nameInput)
            val phoneEntry = findViewById<EditText>(R.id.phoneInput)
            val descriptionEntry  = findViewById<EditText>(R.id.descriptionInput)

            //Load pre-existing info
            nameEntry.setText(retrWitness.name)
            phoneEntry.setText(retrWitness.phone)
            descriptionEntry.setText(retrWitness.description)

            //Preserving pre-existing position
            position = retrWitness.position!!
        }
        else {
            Log.d("debug", "witness info retrieval failed")
        }


        //Retrieve inputs
        val nameEntry = findViewById<EditText>(R.id.nameInput)
        val phoneEntry = findViewById<EditText>(R.id.phoneInput)
        val descriptionEntry = findViewById<EditText>(R.id.descriptionInput)

        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            Log.d("debug message", "save button pressed")
            //Create WitnessInfo object
            var wit = WitnessInfo(
                position,
                nameEntry.getText().toString(),
                phoneEntry.getText().toString(),
                descriptionEntry.getText().toString()
            )

            //Set result to WitnessInfo object and return to previous view
            val intent = Intent()
            intent.putExtra("Witness Info", wit)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}