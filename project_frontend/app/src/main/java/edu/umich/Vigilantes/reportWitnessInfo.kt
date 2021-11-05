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
import android.text.method.ScrollingMovementMethod;

class reportWitnessInfo : AppCompatActivity(), witnessAdapter.OnItemClickListener {
    private val reportWitnesses: MutableList<WitnessInfo> = mutableListOf()
    private val adapter = witnessAdapter(reportWitnesses, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_witness)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val addWitnessButton = findViewById<Button>(R.id.addWitnessButton)

        addWitnessButton.setOnClickListener {
            val witness = WitnessInfo()
            val intent = Intent(this, addWitnessForm::class.java)
            intent.putExtra("Witness Info", witness)    //Parcelize witness info
            addWitness.launch(intent)
        }

        val incidentDescription = findViewById<TextView>(R.id.incidentDescription)
        incidentDescription.movementMethod = ScrollingMovementMethod()
    }

    override fun onItemClick(position: Int) {
        val witness = reportWitnesses[position]
        val intent = Intent(this, witnessPreview::class.java)
        intent.putExtra("Existing Witness", witness)
        editWitness.launch(intent)
    }

    private val addWitness =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == Activity.RESULT_OK) {
                //Retrieve WitnessInfo object from call
                val witness = it.data?.getParcelableExtra<WitnessInfo>("Witness Info")
                Log.d("debug message", "Witness Info received")
                if (witness != null) {
                    witness.name?.let { it1 -> Log.d("Witness added ", it1) }
                    witness.position = reportWitnesses.size  //Update witness position in list
                    Log.d("witness", witness.name + " is now at position " + witness.position)
                    reportWitnesses.add(witness) //Add retrieved witness to list of witness
                    adapter.notifyItemInserted(reportWitnesses.size) //Update last witness in list
                }
            }
            else {
                Log.d("debug message", "Witness Info lost")
            }
        }

    private val editWitness =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //Retrieve WitnessInfo object from call
                val editedWitness = it.data?.getParcelableExtra<WitnessInfo>("Edited Witness")
                Log.d("debug message", "participant edited successfully")
                if (editedWitness != null) {
                    reportWitnesses[editedWitness.position!!] = editedWitness    //Update witness at returned position
                    adapter.notifyItemChanged(editedWitness.position!!) //Update list at returned position
                }
            }
            else if(it.resultCode == 66) {  //If witness is deleted
                //Retrieve WitnessInfo object from call
                val deletedIndex = it.data?.getIntExtra("Delete index", -1)
                Log.d("debug message", "witness at position $deletedIndex deleted")
                if (deletedIndex != null) {
                    reportWitnesses.removeAt(deletedIndex)
                    updatePositions(deletedIndex)
                    adapter.notifyItemRemoved(deletedIndex)
                    adapter.notifyItemRangeChanged(deletedIndex, reportWitnesses.size)
                }
            }
            else if(it.resultCode == Activity.RESULT_CANCELED) {    //If changes are discarded
                Log.d("debug message", "witness edit changes discarded")
            }
            else {
                Log.d("debug message", "witness edit failed")
            }
        }

    //Used to update witness's positions in list after a deletion
    private fun updatePositions(position: Int) {
        for((index, witness) in reportWitnesses.withIndex()) {
            if(index >= position) {
                witness.position = index
                Log.d("debug message", "witness ${witness.name} is now at position ${witness.position}")
            }
        }
    }
}