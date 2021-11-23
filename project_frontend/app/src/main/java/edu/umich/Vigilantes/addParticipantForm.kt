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

class addParticipantForm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addpart_form)

        //Retrieve participant info if participant is existing
        val retrParticipant = intent.getParcelableExtra<ParticipantInfo>("Existing Participant")
        var position = 0
        //If participant is pre-existing
        if (retrParticipant != null) {
            retrParticipant.name?.let { Log.d("participant being edited", it) }
            val nameEntry = findViewById<EditText>(R.id.nameInput)
            val addrEntry = findViewById<EditText>(R.id.addrInput)
            val zipEntry  = findViewById<EditText>(R.id.zipInput)
            val cityEntry = findViewById<EditText>(R.id.cityInput)
            val stateEntry = findViewById<EditText>(R.id.stateInput)
            val licenseEntry = findViewById<EditText>(R.id.licenseInput)
            val phoneEntry = findViewById<EditText>(R.id.phoneInput)

            val insuranceEntry = findViewById<EditText>(R.id.insuranceInput)
            val policyEntry = findViewById<EditText>(R.id.policyInput)
            val expirationEntry = findViewById<EditText>(R.id.expirationInput)
            val agentNumberEntry = findViewById<EditText>(R.id.agentNumberInput)

            //Load pre-existing info
            nameEntry.setText(retrParticipant.name)
            addrEntry.setText(retrParticipant.addr)
            zipEntry.setText(retrParticipant.zip)
            cityEntry.setText(retrParticipant.city)
            stateEntry.setText(retrParticipant.state)
            licenseEntry.setText(retrParticipant.license)
            phoneEntry.setText(retrParticipant.phone)
            insuranceEntry.setText(retrParticipant.insurance)
            policyEntry.setText(retrParticipant.policy)
            expirationEntry.setText(retrParticipant.expiration)
            agentNumberEntry.setText(retrParticipant.agentNumber)

            //Preserving pre-existing position
            position = retrParticipant.position!!
        }
        else {
            Log.d("debug", "participant info retrieval failed")
        }


        //Retrieve inputs
        val nameEntry = findViewById<EditText>(R.id.nameInput)
        val addrEntry = findViewById<EditText>(R.id.addrInput)
        val zipEntry  = findViewById<EditText>(R.id.zipInput)
        val cityEntry = findViewById<EditText>(R.id.cityInput)
        val stateEntry = findViewById<EditText>(R.id.stateInput)
        val licenseEntry = findViewById<EditText>(R.id.licenseInput)
        val phoneEntry = findViewById<EditText>(R.id.phoneInput)

        val insuranceEntry = findViewById<EditText>(R.id.insuranceInput)
        val policyEntry = findViewById<EditText>(R.id.policyInput)
        val expirationEntry = findViewById<EditText>(R.id.expirationInput)
        val agentNumberEntry = findViewById<EditText>(R.id.agentNumberInput)


        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            Log.d("debug message", "save button pressed")
            //Create ParticipantInfo object
            var par = ParticipantInfo(
                position,
                nameEntry.getText().toString(),
                addrEntry.getText().toString(),
                zipEntry.getText().toString(),
                cityEntry.getText().toString(),
                stateEntry.getText().toString(),
                licenseEntry.getText().toString(),
                phoneEntry.getText().toString(),
                insuranceEntry.getText().toString(),
                policyEntry.getText().toString(),
                expirationEntry.getText().toString(),
                agentNumberEntry.getText().toString()
            )

            //Set result to ParticipantInfo object and return to previous view
            val intent = Intent()
            intent.putExtra("Participant Info", par)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}