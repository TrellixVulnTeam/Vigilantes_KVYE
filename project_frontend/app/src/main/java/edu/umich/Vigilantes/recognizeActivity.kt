package edu.umich.Vigilantes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umich.Vigilantes.reportStoreCar.postImagesCar
import android.content.Context
import android.content.Intent
import android.util.Log
import edu.umich.Vigilantes.databinding.ActivityRecognizeBinding
import edu.umich.Vigilantes.reportStoreCar.carLabels
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class recognizeActivity : AppCompatActivity() {
    private lateinit var view: ActivityRecognizeBinding
    private var fromMain: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityRecognizeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_recognize)

        val jsonString = applicationContext.assets.open("easy_labels.json").bufferedReader().use { it.readText() }
        val carMap = JSONObject(jsonString)
        val reportProgress: Bundle = intent.extras!!
        reportProgress.getBoolean("main")?.let{
            fromMain = it
        }
        val car1 = findViewById<TextView>(R.id.predictionOne)
        val car2 = findViewById<TextView>(R.id.predictionTwo)
        val car3 = findViewById<TextView>(R.id.predictionThree)
        val uri: Uri? = reportProgress.getParcelable("carImageUri")

        postImagesCar(applicationContext, uri) { msg ->
            runOnUiThread {
                toast(msg)
                car1.text = carMap[carLabels[0].toString()].toString()
                car2.text = carMap[carLabels[1].toString()].toString()
                car3.text = carMap[carLabels[2].toString()].toString()
            }
        }
        val car1button = findViewById<ImageView>(R.id.carOne)
        val car2button = findViewById<ImageView>(R.id.carTwo)
        val car3button = findViewById<ImageView>(R.id.carThree)

        car1button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[0].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            fromMain?.let{
                proceed.launch(intent)
            } ?: run{
                startActivity(intent)
            }

        }
        car2button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[1].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            fromMain?.let{
                proceed.launch(intent)
            } ?: run{
                startActivity(intent)
            }
        }
        car3button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[2].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            fromMain?.let{
                proceed.launch(intent)
            } ?: run{
                startActivity(intent)
            }
        }



    }
    private val proceed =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == 441) {
                //If report is completed, retrieve report list
                val reportList = it.data?.getParcelableExtra<reportList>("Report List")
                val report = it.data?.getParcelableExtra<reportObj>("Report Info")
                val intent = Intent()
                intent.putExtra("Report List", reportList)
                intent.putExtra("Report Info", report)
                setResult(441, intent)
                finish()
            }
            else {
                Log.d("debug message", "Report List lost")
            }
        }
}