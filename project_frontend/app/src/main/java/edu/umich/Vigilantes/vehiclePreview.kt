package edu.umich.Vigilantes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class vehiclePreview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_preview)

        //Retrieve participant info if participant is existing
        val retrVehicle = intent.getParcelableExtra<VehicleInfo>("Existing Vehicle")
        if(retrVehicle != null) {
            val makemodelPreview = findViewById<TextView>(R.id.makemodelPreview)
            val yearPreview = findViewById<TextView>(R.id.yearPreview)
            val plateNumberPreview = findViewById<TextView>(R.id.plateNumberPreview)
            val VINPreview = findViewById<TextView>(R.id.VINPreview)
            val colorPreview = findViewById<TextView>(R.id.colorPreview)

            //Prevew participant information
            makemodelPreview.setText(retrVehicle.makemodel)
            yearPreview.setText(retrVehicle.year)
            plateNumberPreview.setText(retrVehicle.plateNumber)
            VINPreview.setText(retrVehicle.VIN)
            colorPreview.setText(retrVehicle.color)
        }

        val editButton = findViewById<Button>(R.id.editButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        editButton.setOnClickListener {
            val intent = Intent(this, addVehicleForm::class.java)
            intent.putExtra("Existing Vehicle", retrVehicle)
            editVehicle.launch(intent)
        }

        deleteButton.setOnClickListener {
            val intent = Intent()
            if (retrVehicle != null) {
                intent.putExtra("Delete index", retrVehicle.position)
                setResult(66, intent)
                finish()
            }
        }
    }

    private val editVehicle =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve ParticipantInfo object from call
                val editedVehicle = it.data?.getParcelableExtra<VehicleInfo>("Vehicle Info")
                Log.d("debug message", "vehicle edited successfully")
                if (editedVehicle != null) {
                    intent.putExtra("Edited Vehicle", editedVehicle)
                    setResult(RESULT_OK, intent)
                }
            }
            else if(it.resultCode == Activity.RESULT_CANCELED) {    //If changes are discarded
                setResult(RESULT_CANCELED, intent)
                Log.d("debug message", "vehicle edit changes discarded")
            }
            else {
                Log.d("debug message", "vehicle edit failed")
            }
            finish()
        }
}