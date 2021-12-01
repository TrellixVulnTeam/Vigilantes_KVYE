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

class reportVehicleInfo : AppCompatActivity(), vehicleAdapter.OnItemClickListener {
    private lateinit var report: reportObj
    private lateinit var reportVehicles: MutableList<VehicleInfo>
    private lateinit var adapter: vehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_vehicle)

        //Retrieve current report and report list information
        var reportList = intent.getParcelableExtra<reportList>("Report List")!!
        report = intent.getParcelableExtra("Report Info")!!
        reportVehicles = report?.getVehicles()  //Get vehicles list from report
        adapter = vehicleAdapter(reportVehicles, this)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val addVehicleButton = findViewById<Button>(R.id.addVehicleButton)
        val continueButton = findViewById<Button>(R.id.continueButton)

        addVehicleButton.setOnClickListener {
            val vehicle = VehicleInfo()
            val intent = Intent(this, addVehicleForm::class.java)
            intent.putExtra("Vehicle Info", vehicle)    //Parcelize vehicle info
            addVehicle.launch(intent)
        }

        continueButton.setOnClickListener {
            report?.setVehicles(reportVehicles) //Set updated vehicles list to report
            //Send report to next page
            val intent = Intent(this, reportParticipantInfo::class.java)
            intent.putExtra("Report List", reportList)
            intent.putExtra("Report Info", report)  //Parcelize report
            proceed.launch(intent)
        }
    }

    override fun onItemClick(position: Int) {
        val vehicle = reportVehicles[position]
        val intent = Intent(this, vehiclePreview::class.java)
        intent.putExtra("Existing Vehicle", vehicle)
        editVehicle.launch(intent)
    }

    private val addVehicle =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == Activity.RESULT_OK) {
                //Retrieve VehicleInfo object from call
                val vehicle = it.data?.getParcelableExtra<VehicleInfo>("Vehicle Info")
                Log.d("debug message", "Vehicle Info received")
                if (vehicle != null) {
                    vehicle.makemodel?.let { it1 -> Log.d("Vehicle added ", it1) }
                    vehicle.position = reportVehicles.size  //Update vehicle position in list
                    Log.d("vehicle", vehicle.makemodel + " is now at position " + vehicle.position)
                    reportVehicles.add(vehicle) //Add retrieved vehicle to list of vehicle
                    adapter.notifyItemInserted(reportVehicles.size) //Update last vehicle in list
                }
            }
            else {
                Log.d("debug message", "Vehicle Info lost")
            }
        }

    private val editVehicle =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve VehicleInfo object from call
                val editedVehicle = it.data?.getParcelableExtra<VehicleInfo>("Edited Vehicle")
                Log.d("debug message", "participant edited successfully")
                if (editedVehicle != null) {
                    reportVehicles[editedVehicle.position!!] = editedVehicle    //Update vehicle at returned position
                    adapter.notifyItemChanged(editedVehicle.position!!) //Update list at returned position
                }
            }
            else if(it.resultCode == 66) {  //If vehicle is deleted
                //Retrieve VehicleInfo object from call
                val deletedIndex = it.data?.getIntExtra("Delete index", -1)
                Log.d("debug message", "vehicle at position $deletedIndex deleted")
                if (deletedIndex != null) {
                    reportVehicles.removeAt(deletedIndex)
                    updatePositions(deletedIndex)
                    adapter.notifyItemRemoved(deletedIndex)
                    adapter.notifyItemRangeChanged(deletedIndex, reportVehicles.size)
                }
            }
            else if(it.resultCode == Activity.RESULT_CANCELED) {    //If changes are discarded
                Log.d("debug message", "vehicle edit changes discarded")
            }
            else {
                Log.d("debug message", "vehicle edit failed")
            }
        }

    //Used to update vehicle's positions in list after a deletion
    private fun updatePositions(position: Int) {
        for((index, vehicle) in reportVehicles.withIndex()) {
            if(index >= position) {
                vehicle.position = index
                Log.d("debug message", "vehicle ${vehicle.makemodel} is now at position ${vehicle.position}")
            }
        }
    }

    private val proceed =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == 441) {
                //If report is completed, retrieve report list
                val reportList = it.data?.getParcelableExtra<reportList>("Report List")
                val report = it.data?.getParcelableExtra<reportObj>("Report Info")

                val intent = Intent()
                intent.putExtra("Report List", reportList)
                intent.putExtra("Report Info", report)
                setResult(441, intent)
                finish()
            }
            else {
                Log.d("debug message", "Report List lost")
            }
        }
}