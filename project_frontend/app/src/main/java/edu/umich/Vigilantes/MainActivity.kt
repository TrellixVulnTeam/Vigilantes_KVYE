package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.widget.Button
import android.widget.TextView
import java.util.Date;


//import java.text.SimpleDateFormat;
//import java.util.Date;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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

        //Find past reports button
        var reportsButton = findViewById<Button>(R.id.pastReportButton)

        //Listener for past reports click
        reportsButton.setOnClickListener {
            val intent = Intent(this, pastReports::class.java)
            startActivity(intent)
        }
    }
}