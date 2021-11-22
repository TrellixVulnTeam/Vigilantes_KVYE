package edu.umich.Vigilantes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umich.Vigilantes.reportStoreCar.postImagesCar
import android.content.Context
import android.content.Intent
import edu.umich.Vigilantes.databinding.ActivityRecognizeBinding
import edu.umich.Vigilantes.reportStoreCar.carLabels
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class recognizeActivity : AppCompatActivity() {
    private lateinit var view: ActivityRecognizeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityRecognizeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_recognize)
        val jsonString = applicationContext.assets.open("easy_labels.json").bufferedReader().use { it.readText() }
        val carMap = JSONObject(jsonString)
        val reportProgress: Bundle = intent.extras!!
        val car1 = findViewById<TextView>(R.id.predictionOne)
        val car2 = findViewById<TextView>(R.id.predictionTwo)
        val car3 = findViewById<TextView>(R.id.predictionThree)
        val uri: Uri? = reportProgress.getParcelable("car")!!
        postImagesCar(applicationContext,uri) { msg ->
            runOnUiThread {
                toast(msg)
                car1.text = carMap[carLabels[0].toString()].toString()
                car2.text = carMap[carLabels[1].toString()].toString()
                car3.text = carMap[carLabels[2].toString()].toString()
            }
        }
        val car1button = findViewById<Button>(R.id.carOne)
        val car2button = findViewById<Button>(R.id.carTwo)
        val car3button = findViewById<Button>(R.id.carThree)
        car1button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[0].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            startActivity(intent)
        }
        car2button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[1].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            startActivity(intent)
        }
        car3button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[2].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            startActivity(intent)
        }



    }
}