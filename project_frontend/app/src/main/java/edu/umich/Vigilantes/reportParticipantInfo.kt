package edu.umich.Vigilantes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.umich.Vigilantes.databinding.ActivityMainBinding
import edu.umich.Vigilantes.databinding.ActivityReportParticipantBinding
import kotlinx.android.synthetic.main.activity_report_participant.*

class reportParticipantInfo : AppCompatActivity(), participantAdapter.OnItemClickListener {
    private lateinit var report: reportObj
    private lateinit var reportParticipants: MutableList<ParticipantInfo>
    private lateinit var adapter: participantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_participant)

        //Retrieve current report information
        report = intent.getParcelableExtra("Report Info")!!
        reportParticipants = report?.getParticipants()  //Get participants list from report
        adapter = participantAdapter(reportParticipants, this)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val addParticipantButton = findViewById<Button>(R.id.addParticipantButton)
        val continueButton = findViewById<Button>(R.id.continueButton)

        addParticipantButton.setOnClickListener {
            val participant = ParticipantInfo()
            val intent = Intent(this, addParticipantForm::class.java)
            intent.putExtra("Participant info", participant)    //Parcelize participant info
            addParticipant.launch(intent)
        }

        continueButton.setOnClickListener {
            report?.setParticipants(reportParticipants) //Set updated vehicles list to report
            //Send report to next page
            val intent = Intent(this, reportWitnessInfo::class.java)
            intent.putExtra("Report Info", report)  //Parcelize report
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {
        val participant = reportParticipants[position]
        val intent = Intent(this, participantPreview::class.java)
        intent.putExtra("Existing Participant", participant)
        editParticipant.launch(intent)
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
                    participant.position = reportParticipants.size  //Update participant position in list
                    Log.d("participant", participant.name + " is now at position " + participant.position)
                    reportParticipants.add(participant) //Add retrieved participant to list of participants
                    adapter.notifyItemInserted(reportParticipants.size) //Update last participant in list
                }
            }
            else {
                Log.d("debug message", "Participant Info lost")
            }
        }

    private val editParticipant =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve ParticipantInfo object from call
                val editedParticipant = it.data?.getParcelableExtra<ParticipantInfo>("Edited Participant")
                Log.d("debug message", "participant edited successfully")
                if (editedParticipant != null) {
                    reportParticipants[editedParticipant.position!!] = editedParticipant    //Update participant at returned position
                    adapter.notifyItemChanged(editedParticipant.position!!) //Update list at returned position
                }
            }
            else if(it.resultCode == 66) {  //If participant is deleted
                //Retrieve ParticipantInfo object from call
                val deletedIndex = it.data?.getIntExtra("Delete index", -1)
                Log.d("debug message", "participant at position $deletedIndex deleted")
                if (deletedIndex != null) {
                    reportParticipants.removeAt(deletedIndex)
                    updatePositions(deletedIndex)
                    adapter.notifyItemRemoved(deletedIndex)
                    adapter.notifyItemRangeChanged(deletedIndex, reportParticipants.size)
                }
            }
            else if(it.resultCode == Activity.RESULT_CANCELED) {    //If changes are discarded
                Log.d("debug message", "participant edit changes discarded")
            }
            else {
                Log.d("debug message", "participant edit failed")
            }
        }

    //Used to update participant's positions in list after a deletion
    private fun updatePositions(position: Int) {
        for((index, participant) in reportParticipants.withIndex()) {
            if(index >= position) {
                participant.position = index
                Log.d("debug message", "participant ${participant.name} is now at position ${participant.position}")
            }
        }
    }
}