package edu.umich.Vigilantes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class addParticipantForm : AppCompatActivity() {
    private var popupExists = false

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
        val disdelButton = findViewById<Button>(R.id.disdelButton)

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

        disdelButton.setOnClickListener {
            if(!popupExists) {
                createPopup(position)
            }
        }
    }

    private fun createPopup(position: Int) {
        //Initialize popup window
        popupExists = true
        Log.d("createPopUp", "Calling createPopUp")
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.disdel_popup_box, null)
        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        //Set popup window on top of parent view
        popupWindow.elevation = 10.0F

        val slideIn = Slide()
        slideIn.slideEdge = Gravity.START
        popupWindow.exitTransition = slideIn

        val slideOut = Slide()
        slideOut.slideEdge = Gravity.RIGHT
        popupWindow.exitTransition = slideOut

        val discardOpt = view.findViewById<Button>(R.id.discardOption)
        val deleteOpt = view.findViewById<Button>(R.id.deleteOption)

        discardOpt.setOnClickListener {
            popupExists = false
            popupWindow.dismiss()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        deleteOpt.setOnClickListener {
            popupExists = false
            popupWindow.dismiss()

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

            val intent = Intent()
            intent.putExtra("Participant Info", par)
            setResult(66, intent)
            finish()
        }

        //Display popup window
        var root_layout = findViewById<ConstraintLayout>(R.id.root_layout)
        popupWindow.showAtLocation(root_layout, Gravity.CENTER, 0, 0)
    }
}