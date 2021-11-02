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
    private val reportParticipants: MutableList<ParticipantInfo> = mutableListOf()
    private val adapter = participantAdapter(reportParticipants, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_participant)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val addParticipantButton = findViewById<Button>(R.id.addParticipantButton)

        addParticipantButton.setOnClickListener {
            val participant = ParticipantInfo()
            val intent = Intent(this, addParticipantForm::class.java)
            intent.putExtra("Participant info", participant)    //Parcelize participant info
            addParticipant.launch(intent)
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem: ParticipantInfo = reportParticipants[position]
        Log.d("Message", "Participant " + clickedItem.name + " clicked")
        val intent = Intent(this, addParticipantForm::class.java)
        intent.putExtra("Existing Participant", clickedItem)    //Parcelize participant info
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
                val editedParticipant = it.data?.getParcelableExtra<ParticipantInfo>("Participant Info")
                Log.d("debug message", "participant edited successfully")
                if (editedParticipant != null) {
                    reportParticipants[editedParticipant.position!!] = editedParticipant    //Update participant at returned position
                    adapter.notifyItemChanged(editedParticipant.position!!) //Update list at returned position
                }
            }
            else if(it.resultCode == 66) {  //If participant is deleted
                //Retrieve ParticipantInfo object from call
                val deletedParticipant = it.data?.getParcelableExtra<ParticipantInfo>("Participant Info")
                if (deletedParticipant?.position != null) {
                    Log.d("debug message", "participant at position " + deletedParticipant.position + " deleted")
                    reportParticipants.removeAt(deletedParticipant.position!!)
                    updatePositions(deletedParticipant.position!!)
                    adapter.notifyItemRemoved(deletedParticipant.position!!)
                    adapter.notifyItemRangeChanged(deletedParticipant.position!!, reportParticipants.size)
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