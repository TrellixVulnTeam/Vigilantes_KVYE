package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.view.View
import edu.umich.Vigilantes.databinding.ActivityMainBinding
import android.icu.text.SimpleDateFormat
import android.widget.Button
import android.widget.TextView
import java.util.Date;


//import java.text.SimpleDateFormat;
//import java.util.Date;

class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        //Find past reports button
        var reportsButton = findViewById<Button>(R.id.pastReportButton)

        //Listener for past reports click
        reportsButton.setOnClickListener {
            val intent = Intent(this, pastReports::class.java)
            startActivity(intent)
        }

        view.idgotoPDF.setOnClickListener {
            print("Headed to pdf activity")
            startActivity(Intent(this, pdfActivity::class.java))
        }
    }

    fun genPDF(view: View?) = startActivity(Intent(this, pdfActivity::class.java))
}