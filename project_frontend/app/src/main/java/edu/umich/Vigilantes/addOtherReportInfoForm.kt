package edu.umich.Vigilantes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class addOtherReportInfoForm : AppCompatActivity() {
    private lateinit var report: reportObj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_other_report_info_form)

        //Retrieve report list
        var reportList = intent.getParcelableExtra<reportList>("Report List")!!
        report = intent.getParcelableExtra("Report Info")!!

        val locationEntry = findViewById<EditText>(R.id.locationInput)
        val datetimeEntry = findViewById<EditText>(R.id.datetimeInput)
        val descriptionEntry  = findViewById<EditText>(R.id.descriptionInput)

        //Load pre-existing info
        if (report != null) {
            locationEntry.setText(report.getLoc())
            datetimeEntry.setText(report.getDateTime())
            descriptionEntry.setText(report.getDesc())
        }

        val generateReportButton = findViewById<Button>(R.id.generateReportButton)

        generateReportButton.setOnClickListener {
            //Add info to report
            report?.setLoc(locationEntry.getText().toString())
            report?.setDateTime(datetimeEntry.getText().toString())
            report?.setDesc(descriptionEntry.getText().toString())
            Log.d("debug message", report.getLoc() + report.getDateTime() + report.getDesc())

            //Check if report exists
            if(reportList.find(report) != -1) {
                Log.d("debug message", "report exists and will be edited")
                reportList.editReport(report)       //If exists, edit report
            }
            else {
                Log.d("debug message", "report does not exist and will be added")
                reportList.addReport(report)        //If not exists, add report
            }

            //Return updated report/list to MainActivity
            val intent = Intent()
            intent.putExtra("Report List", reportList)
            //intent.putExtra("Report Info", report)  //Parcelize report
            setResult(441, intent)
            finish()
        }
    }
}