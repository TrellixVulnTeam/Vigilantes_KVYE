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

class witnessPreview : AppCompatActivity() {
    private var popupExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_witness_preview)

        //Retrieve witness info if participant is existing
        val retrWitness = intent.getParcelableExtra<WitnessInfo>("Existing Witness")
        if(retrWitness != null) {
            val namePreview = findViewById<TextView>(R.id.namePreview)
            val phonePreview = findViewById<TextView>(R.id.phonePreview)
            val descriptionPreview = findViewById<TextView>(R.id.descriptionPreview)

            //Preview witness information
            namePreview.setText(retrWitness.name)
            phonePreview.setText(retrWitness.phone)
            descriptionPreview.setText(retrWitness.description)
        }

        val editButton = findViewById<Button>(R.id.editButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        editButton.setOnClickListener {
            val intent = Intent(this, addWitnessForm::class.java)
            intent.putExtra("Existing Witness", retrWitness)
            editWitness.launch(intent)
        }

        deleteButton.setOnClickListener {
            if (retrWitness != null) {
                createPopUp(retrWitness)
            }
        }
    }

    private val editWitness =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve WitnessInfo object from call
                val editedWitness = it.data?.getParcelableExtra<WitnessInfo>("Witness Info")
                Log.d("debug message", "witness edited successfully")
                if (editedWitness != null) {
                    intent.putExtra("Edited Witness", editedWitness)
                    setResult(RESULT_OK, intent)
                }
            }
            else if(it.resultCode == Activity.RESULT_CANCELED) {    //If changes are discarded
                setResult(RESULT_CANCELED, intent)
                Log.d("debug message", "witness edit changes discarded")
            }
            else {
                Log.d("debug message", "witness edit failed")
            }
            finish()
        }

    private fun createPopUp(retrWitness: WitnessInfo) {
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
            if (retrWitness != null) {
                intent.putExtra("Delete index", retrWitness.position)
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