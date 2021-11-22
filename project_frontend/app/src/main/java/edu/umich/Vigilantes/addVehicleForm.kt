package edu.umich.Vigilantes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class addVehicleForm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addvehicle_form)

        //Retrieve vehicle info if vehicle is existing
        val retrVehicle = intent.getParcelableExtra<VehicleInfo>("Existing Vehicle")
        var position = 0
        //If vehicle is pre-existing
        if (retrVehicle != null) {
            retrVehicle.makemodel?.let { Log.d("Vehicle being edited", it) }
            val makemodelEntry = findViewById<EditText>(R.id.makemodelInput)
            val yearEntry = findViewById<EditText>(R.id.yearInput)
            val plateNumberEntry  = findViewById<EditText>(R.id.plateNumberInput)
            val VINEntry = findViewById<EditText>(R.id.VINInput)
            val colorEntry = findViewById<EditText>(R.id.colorInput)

            //Load pre-existing info
            makemodelEntry.setText(retrVehicle.makemodel)
            yearEntry.setText(retrVehicle.year)
            plateNumberEntry.setText(retrVehicle.plateNumber)
            VINEntry.setText(retrVehicle.VIN)
            colorEntry.setText(retrVehicle.color)

            //Preserving pre-existing position
            position = retrVehicle.position!!
        }
        else {
            Log.d("debug", "vehicle info retrieval failed")
        }


        //Retrieve inputs
        val makemodelEntry = findViewById<EditText>(R.id.makemodelInput)
        val yearEntry = findViewById<EditText>(R.id.yearInput)
        val plateNumberEntry  = findViewById<EditText>(R.id.plateNumberInput)
        val VINEntry = findViewById<EditText>(R.id.VINInput)
        val colorEntry = findViewById<EditText>(R.id.colorInput)

        val saveButton = findViewById<ImageButton>(R.id.saveButton)
        val discardButton = findViewById<ImageButton>(R.id.disdelButton)

        saveButton.setOnClickListener {
            Log.d("debug message", "save button pressed")
            //Create VehicleInfo object
            var veh = VehicleInfo(
                position,
                makemodelEntry.getText().toString(),
                yearEntry.getText().toString(),
                plateNumberEntry.getText().toString(),
                VINEntry.getText().toString(),
                colorEntry.getText().toString()
            )

            //Set result to VehicleInfo object and return to previous view
            val intent = Intent()
            intent.putExtra("Vehicle Info", veh)
            setResult(RESULT_OK, intent)
            finish()
        }

        discardButton.setOnClickListener {
            Log.d("debug message", "discard button pressed")
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }
}