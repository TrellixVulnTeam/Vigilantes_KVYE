package edu.umich.Vigilantes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class addParticipantForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addpart_form)

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
            //Create ParticipantInfo object
            var par = ParticipantInfo(
                0,
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