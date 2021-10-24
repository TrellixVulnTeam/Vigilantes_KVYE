package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.icu.text.SimpleDateFormat
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

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                toast("Fine location access denied", false)
                finish()
            }
        }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}