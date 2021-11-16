package edu.umich.Vigilantes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class witnessPreview : AppCompatActivity() {

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
            val intent = Intent()
            if (retrWitness != null) {
                intent.putExtra("Delete index", retrWitness.position)
                setResult(66, intent)
                finish()
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
}