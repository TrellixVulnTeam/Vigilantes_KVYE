package edu.umich.Vigilantes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class participantPreview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_preview)

        //Retrieve participant info if participant is existing
        val retrParticipant = intent.getParcelableExtra<ParticipantInfo>("Existing Participant")
        if(retrParticipant != null) {
            val namePreview = findViewById<TextView>(R.id.namePreview)
            val AddrPreview = findViewById<TextView>(R.id.AddrPreview)
            val zipPreview = findViewById<TextView>(R.id.zipPreview)
            val cityPreview = findViewById<TextView>(R.id.cityPreview)
            val statePreview = findViewById<TextView>(R.id.statePreview)
            val licensePreview = findViewById<TextView>(R.id.licensePreview)
            val phonePreview = findViewById<TextView>(R.id.phonePreview)
            val insurancePreview = findViewById<TextView>(R.id.insurancePreview)
            val policyPreview = findViewById<TextView>(R.id.policyPreview)
            val expirationPreview = findViewById<TextView>(R.id.expirationPreview)
            val agentNumberPreview = findViewById<TextView>(R.id.agentNumberPreview)

            //Prevew participant information
            namePreview.setText(retrParticipant.name)
            AddrPreview.setText(retrParticipant.addr)
            zipPreview.setText(retrParticipant.zip)
            cityPreview.setText(retrParticipant.city)
            statePreview.setText(retrParticipant.state)
            licensePreview.setText(retrParticipant.license)
            phonePreview.setText(retrParticipant.phone)
            insurancePreview.setText(retrParticipant.insurance)
            policyPreview.setText(retrParticipant.policy)
            expirationPreview.setText(retrParticipant.expiration)
            agentNumberPreview.setText(retrParticipant.agentNumber)
        }

        val editButton = findViewById<Button>(R.id.editButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        editButton.setOnClickListener {
            val intent = Intent(this, addParticipantForm::class.java)
            intent.putExtra("Existing Participant", retrParticipant)
            editParticipant.launch(intent)
        }

        deleteButton.setOnClickListener {
            val intent = Intent()
            if (retrParticipant != null) {
                intent.putExtra("Delete index", retrParticipant.position)
                setResult(66, intent)
                finish()
            }
        }
    }

    private val editParticipant =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve ParticipantInfo object from call
                val editedParticipant = it.data?.getParcelableExtra<ParticipantInfo>("Participant Info")
                Log.d("debug message", "participant edited successfully")
                if (editedParticipant != null) {
                    intent.putExtra("Edited Participant", editedParticipant)
                    setResult(RESULT_OK, intent)
                }
            }
            else if(it.resultCode == Activity.RESULT_CANCELED) {    //If changes are discarded
                setResult(RESULT_CANCELED, intent)
                Log.d("debug message", "participant edit changes discarded")
            }
            else {
                Log.d("debug message", "participant edit failed")
            }
            finish()
        }
}