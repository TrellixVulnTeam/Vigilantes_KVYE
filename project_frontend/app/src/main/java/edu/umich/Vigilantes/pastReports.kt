package edu.umich.Vigilantes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_report_participant.*

class pastReports : AppCompatActivity(), reportListAdapter.OnItemClickListener {
    private lateinit var adapter: reportListAdapter
    private lateinit var reportList: reportList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_report)

        //Retrieve current report and report list information
        reportList = intent.getParcelableExtra("Report List")!!
        adapter = reportListAdapter(reportList, this)

        Log.d("debug message", "report list size is " + reportList.getLength())

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        //Find home button
        var homeButton = findViewById<ImageButton>(R.id.homeButton)

        //Listener for home click
        homeButton.setOnClickListener {
            finish()    //End current activity
        }
    }

    //Override native back button
    override fun onBackPressed() {
        finish()    //End current activity
    }

    override fun onItemClick(position: Int) {
        val report = reportList.getList()[position]
        val intent = Intent(this, reportPreview::class.java)
        intent.putExtra("Report Info", report)
        intent.putExtra("Report List", reportList)
        proceed.launch(intent)
    }

    private val proceed =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == 441) {
                //If report is completed, retrieve report list
                reportList = it.data?.getParcelableExtra("Report List")!!

                //Update recyclerview
                adapter = reportListAdapter(reportList, this)
                adapter.notifyDataSetChanged()
                recycler_view.adapter = adapter

                val intent = Intent()
                intent.putExtra("Report List", reportList)
                setResult(441, intent)
            }
            else {
                Log.d("debug message", "Report List lost")
            }
        }
}