package edu.umich.Vigilantes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class participantPreview : AppCompatActivity() {
    private var popupExists = false

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

        val editButton = findViewById<ImageButton>(R.id.editButton)
        val deleteButton = findViewById<ImageButton>(R.id.deleteButton)

        editButton.setOnClickListener {
            val intent = Intent(this, addParticipantForm::class.java)
            intent.putExtra("Existing Participant", retrParticipant)
            editParticipant.launch(intent)
        }

        deleteButton.setOnClickListener {
            if (retrParticipant != null) {
                createPopUp(retrParticipant)
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

    private fun createPopUp(retrParticipant: ParticipantInfo) {
        //Initialize popup window
        Log.d("createPopUp", "Calling createPopUp")
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.delete_popup_box, null)
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

        val deleteOpt = view.findViewById<Button>(R.id.DeleteOption)
        val cancelOpt = view.findViewById<Button>(R.id.CancelOption)
        //Delete option chosen
        deleteOpt.setOnClickListener{
            popupWindow.dismiss()
            popupExists = false

            val intent = Intent()
            if (retrParticipant != null) {
                intent.putExtra("Delete index", retrParticipant.position)
                setResult(66, intent)
                finish()
            }
        }
        //Cancel option chosen
        cancelOpt.setOnClickListener{
            popupWindow.dismiss()
            popupExists = false
        }

        //Display popup window
        var root_layout = findViewById<ConstraintLayout>(R.id.root_layout)
        popupWindow.showAtLocation(root_layout, Gravity.CENTER, 0, 0)
    }
}