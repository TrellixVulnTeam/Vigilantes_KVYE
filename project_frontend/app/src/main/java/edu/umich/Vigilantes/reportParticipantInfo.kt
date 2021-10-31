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
                    adapter.notifyItemInserted(reportParticipants.size) //Update list
                }
            }
            else {
                Log.d("debug message", "Participant Info lost")
            }
        }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_LONG).show()
        val clickedItem: ParticipantInfo = reportParticipants[position]
    }
}