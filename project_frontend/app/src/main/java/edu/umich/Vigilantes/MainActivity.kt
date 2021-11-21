package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.view.View
import edu.umich.Vigilantes.databinding.ActivityMainBinding
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.util.Date;


//import java.text.SimpleDateFormat;
//import java.util.Date;

class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding
    private var reportList: reportList = reportList()    //List of reports

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ONCREATE", "onCreate for MainActivity")
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.date)
        val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateandTime: String = sdf.format(Date())
        textView.text = currentDateandTime

        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
                if (!it.value) {
                    toast("${it.key} access denied")
                    finish()
                }
            }
        }.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE))

        //Retrieve list of reports
        if(intent.extras != null) {
            reportList = intent.getParcelableExtra("Report List")!!
        }

        //Find buttons
        var carButton = findViewById<Button>(R.id.addCarButton)
        var plateButton = findViewById<Button>(R.id.addLicensePlateButton)
        var vinButton = findViewById<Button>(R.id.addVinNumberButton)
        var reportsButton = findViewById<Button>(R.id.getPastReports)
        var debugButton = findViewById<Button>(R.id.debugButton)
        var locationButton = findViewById<Button>(R.id.addLocationButton)


        //Listener button clicks
        carButton.setOnClickListener {
            val intent = Intent(this, addVehicle::class.java)
            startActivity(intent)
        }
        plateButton.setOnClickListener {
            val intent = Intent(this, addVehicle::class.java)
            startActivity(intent)
        }
        vinButton.setOnClickListener {
            val intent = Intent(this, addVehicle::class.java)
            startActivity(intent)
        }
        reportsButton.setOnClickListener {
            val intent = Intent(this, pastReports::class.java)
            intent.putExtra("Report List", reportList)
            startActivity(intent)
        }
        locationButton.setOnClickListener {
            val intent = Intent(this, addLoc::class.java)
            startActivity(intent)
        }
        debugButton.setOnClickListener {
            var report = reportObj()
            val intent = Intent(this, reportVehicleInfo::class.java)   //Change page to page being tested
            intent.putExtra("Report List", reportList)
            intent.putExtra("Report Info", report)
            startReport.launch(intent)
        }
    }

    fun genPDF(view: View?) = startActivity(Intent(this, pdfActivity::class.java))

    private val startReport =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == 441) {
                Log.d("debug message", "reports should be received")
                //If report is completed, retrieve latest report
                reportList = it.data?.getParcelableExtra("Report List")!!
                /*
                //If new report exists, add to report list and go to preview
                if(report != null) {
                    reportList.addReport(report)

                    val intent = Intent(this, reportPreview::class.java)
                    intent.putExtra("Report Info", report)
                    intent.putExtra("Report List", reportList)
                    startActivity(intent)
                }*/
            }
            else {
                Log.d("debug message", "Report List lost")
            }
        }
}