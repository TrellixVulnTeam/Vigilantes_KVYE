package edu.umich.Vigilantes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class reportParticipantInfo : AppCompatActivity() {
    private var reportParticipants: MutableList<ParticipantInfo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_participant)

        val addParticipantButton = findViewById<Button>(R.id.addParticipantButton)

        addParticipantButton.setOnClickListener {
            val participant = ParticipantInfo()
            val intent = Intent(this, addParticipantForm::class.java)
            intent.putExtra("Participant info", participant)
            addParticipant.launch(intent)
        }
    }

    private val addParticipant =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == Activity.RESULT_OK) {
                //Retrieve ParticipantInfo object from call
                val participant = it.data?.getParcelableExtra<ParticipantInfo>("Participant Info")
                Log.d("debug message", "Participant Info received")
                if (participant != null) {
                    participant.name?.let { it1 -> Log.d("Participant added ", it1) }
                    reportParticipants.add(participant) //Add retrieved participant to list of participants
                    updateListDisplay()
                }
            }
            else {
                Log.d("debug message", "Participant Info lost")
            }
        }

    private fun updateListDisplay() {
        for(participant in reportParticipants) {
            if(participant.id?.let { findViewById<Button>(it) } == null) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                button.text = participant.name

                val id = View.generateViewId();

                button.setId(id)
                participant.id = id
                Log.d("uid", participant.id.toString())


                val listDisplay = findViewById<LinearLayout>(R.id.participantList)
                listDisplay.addView(button)
            }
        }
    }
}