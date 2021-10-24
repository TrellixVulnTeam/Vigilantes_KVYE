package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.view.View
import edu.umich.Vigilantes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                toast("Fine location access denied", false)
                finish()
            }
        }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        view.idgotoPDF.setOnClickListener {
            startActivity(Intent(this, pdfActivity::class.java))
        }
    }

    fun genPDF(view: View?) = startActivity(Intent(this, pdfActivity::class.java))
}