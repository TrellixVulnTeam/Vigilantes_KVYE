package edu.umich.Vigilantes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
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
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import java.lang.Exception
import java.util.*

class addOtherReportInfoForm : AppCompatActivity() {
    private lateinit var report: reportObj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_other_report_info_form)

        //Retrieve report list
        var reportList = intent.getParcelableExtra<reportList>("Report List")!!
        report = intent.getParcelableExtra("Report Info")!!

        //Retrieve datetime
        val sdf = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z")
        val currentDateandTime: String = sdf.format(Date())

        //Set datetime
        report.setDateTime(currentDateandTime)

        //Display report information
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        val gcd = Geocoder(
            baseContext,
            Locale.getDefault()
        )
        var addresses: List<Address?>

        try {
            if (gps_loc != null) {
                addresses = gcd.getFromLocation(
                    gps_loc.latitude,
                    gps_loc.longitude, 1
                )
                if (addresses.size > 0) {
                    val address =
                        addresses[0]!!.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    val locality = addresses[0]!!.locality
                    val state = addresses[0]!!.adminArea
                    val country = addresses[0]!!.countryName
                    val postalCode = addresses[0]!!.postalCode
                    report.setLoc("$locality $state, $postalCode $country") //Set report location
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
            intent.putExtra("Report Info", report)  //Parcelize report
            setResult(441, intent)
            finish()
        }
    }
}