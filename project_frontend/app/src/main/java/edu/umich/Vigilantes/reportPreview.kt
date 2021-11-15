package edu.umich.Vigilantes

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_report_preview.*

class reportPreview : AppCompatActivity() {
    //Retrieve current report information
    private lateinit var report: reportObj
    private lateinit var reportVehicles: MutableList<VehicleInfo>
    private lateinit var reportParticipants: MutableList<ParticipantInfo>
    private lateinit var reportWitnesses: MutableList<WitnessInfo>

    private lateinit var vAdapter: fullVehicleAdapter
    private lateinit var pAdapter: fullParticipantAdapter
    private lateinit var wAdapter: fullWitnessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_preview)

        //Retrieve current report information
        report = intent.getParcelableExtra("Report Info")!!
        reportVehicles = report?.getVehicles()  //Get vehicles list from report
        reportParticipants = report?.getParticipants()  //Get participants list from report
        reportWitnesses = report?.getWitnesses()  //Get witnesses list from report

        vAdapter = fullVehicleAdapter(reportVehicles)
        pAdapter = fullParticipantAdapter(reportParticipants)
        wAdapter = fullWitnessAdapter(reportWitnesses)

        //Get report information
        val location = findViewById<TextView>(R.id.locationPreview)
        val time = findViewById<TextView>(R.id.timePreview)
        val incidentDesc = findViewById<TextView>(R.id.incidentDescription)

        //Display report information
        location.setText(report?.getLoc()?: "N/A")
        time.setText(report?.getDateTime()?: "N/A")
        incidentDesc.setText(report?.getDesc()?: "")

        //Set recycler view adapters
        vehicle_recycler_view.adapter = vAdapter
        vehicle_recycler_view.layoutManager = LinearLayoutManager(this)
        vehicle_recycler_view.setHasFixedSize(true)

        participant_recycler_view.adapter = pAdapter
        participant_recycler_view.layoutManager = LinearLayoutManager(this)
        participant_recycler_view.setHasFixedSize(true)

        witness_recycler_view.adapter = wAdapter
        witness_recycler_view.layoutManager = LinearLayoutManager(this)
        witness_recycler_view.setHasFixedSize(true)

        val editButton = findViewById<Button>(R.id.editButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        val exportButton = findViewById<Button>(R.id.exportButton)

        editButton.setOnClickListener {

        }

        deleteButton.setOnClickListener {

        }

        exportButton.setOnClickListener {

        }
    }
}