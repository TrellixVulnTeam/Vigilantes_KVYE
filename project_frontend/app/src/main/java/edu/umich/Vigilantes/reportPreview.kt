package edu.umich.Vigilantes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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

    private var popupExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_preview)

        //Retrieve current report information
        report = intent.getParcelableExtra("Report Info")!!
        var reportList = intent.getParcelableExtra<reportList>("Report List")!!
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
        location.text = report?.getLoc()?: ""
        time.text = report?.getDateTime()?: ""
        incidentDesc.text = report?.getDesc()?: ""

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
            //Return to vehicles page
            val intent = Intent(this, reportVehicleInfo::class.java)
            intent.putExtra("Report List", reportList)
            intent.putExtra("Report Info", report)  //Parcelize report
            proceed.launch(intent)
        }

        deleteButton.setOnClickListener {
            if(!popupExists) {
                createPopUp(reportList)
                popupExists = true
            }
        }

        exportButton.setOnClickListener {

        }
    }

    private fun createPopUp(reportList: reportList) {
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
            reportList.deleteReport(report) //Delete report

            popupWindow.dismiss()
            popupExists = false

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Report List", reportList)  //Parcelize report
            startActivity(intent)
            finish()
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
    private val proceed =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == 441) {
                //If report is completed, retrieve report list
                val reportList = it.data?.getParcelableExtra<reportList>("Report List")
                //val report = it.data?.getParcelableExtra<reportObj>("Report Info")

                val intent = Intent()
                intent.putExtra("Report List", reportList)
                setResult(441, intent)
                finish()
            }
            else {
                Log.d("debug message", "Report List lost")
            }
        }
}