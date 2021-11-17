package edu.umich.Vigilantes

import android.app.Activity
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

class vehiclePreview : AppCompatActivity() {
    private var popupExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_preview)

        //Retrieve vehicle info if participant is existing
        val retrVehicle = intent.getParcelableExtra<VehicleInfo>("Existing Vehicle")
        if(retrVehicle != null) {
            val makemodelPreview = findViewById<TextView>(R.id.makemodelPreview)
            val yearPreview = findViewById<TextView>(R.id.yearPreview)
            val plateNumberPreview = findViewById<TextView>(R.id.plateNumberPreview)
            val VINPreview = findViewById<TextView>(R.id.VINPreview)
            val colorPreview = findViewById<TextView>(R.id.colorPreview)

            //Preview vehicle information
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
            if (retrVehicle != null) {
                createPopUp(retrVehicle)
            }
        }
    }

    private val editVehicle =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve VehicleInfo object from call
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

    private fun createPopUp(retrVehicle: VehicleInfo) {
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
            if (retrVehicle != null) {
                intent.putExtra("Delete index", retrVehicle.position)
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