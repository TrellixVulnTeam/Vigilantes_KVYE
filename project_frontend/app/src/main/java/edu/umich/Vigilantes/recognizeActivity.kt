package edu.umich.Vigilantes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umich.Vigilantes.reportStoreCar.postImagesCar
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri.fromFile
import android.util.Log
import edu.umich.Vigilantes.databinding.ActivityRecognizeBinding
import edu.umich.Vigilantes.reportStoreCar.carLabels
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONObject
import java.io.File
import android.graphics.BitmapFactory
import android.view.View

import java.io.IOException


class recognizeActivity : AppCompatActivity() {
    private lateinit var view: ActivityRecognizeBinding
    private var fromMain: Boolean = false
    private var carOneUri: Uri? = null
    private var carTwoUri: Uri? = null
    private var carThreeUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityRecognizeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_recognize)

        val jsonString = applicationContext.assets.open("easy_labels.json").bufferedReader().use { it.readText() }
        val carMap = JSONObject(jsonString)
        val reportProgress: Bundle = intent.extras!!
        fromMain = reportProgress.getBoolean("main")
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
                /*val fileOne = "examples/" + car1.text as String + ".jpg"
                val fileTwo = "examples/" + car2.text as String + ".jpg"
                val fileThree = "examples/" + car3.text as String + ".jpg"
                var carBitmap: Bitmap? = null
                try {
                    val istr = applicationContext.assets.open(fileOne)
                    carBitmap = BitmapFactory.decodeStream(istr)
                    val scaledBitmap = Bitmap.createScaledBitmap(carBitmap,400,140,true)
                    //view.carOne.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
                    view.carOne.setImageBitmap(scaledBitmap)
                } catch (e: IOException) {

                }
                try {
                    val istr = applicationContext.assets.open(fileTwo)
                    carBitmap = BitmapFactory.decodeStream(istr)
                    val scaledBitmap = Bitmap.createScaledBitmap(carBitmap,400,140,true)
                    view.carTwo.setImageBitmap(scaledBitmap)
                } catch (e: IOException) {

                }
                try {
                    val istr = applicationContext.assets.open(fileThree)
                    carBitmap = BitmapFactory.decodeStream(istr)
                    val scaledBitmap = Bitmap.createScaledBitmap(carBitmap,400,140,true)
                    view.carThree.setImageBitmap(null)
                    view.carThree.setImageBitmap(scaledBitmap)
                } catch (e: IOException) {

                }
                val file : File? = null
                carOneUri = fromFile(File())
                carTwoUri = fromFile(File(fileTwo))
                carThreeUri = fromFile(File(fileThree))
                carOneUri?.let {
                    view.carOne.setImageURI(null);
                    view.carOne.setImageURI(it)
                }
                carTwoUri?.let{
                    view.carTwo.setImageURI(null);
                    view.carTwo.setImageURI(it)
                }
                carThreeUri?.let{
                    view.carThree.setImageURI(null);
                    view.carThree.setImageURI(it)
                }*/
            }

        }

        val car1button = findViewById<Button>(R.id.carOne)
        val car2button = findViewById<Button>(R.id.carTwo)
        val car3button = findViewById<Button>(R.id.carThree)


        car1button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[0].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            if(fromMain){
                proceed.launch(intent)
            }
            else{
                setResult(441, intent)
                finish()
            }

        }
        car2button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[1].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            if(fromMain){
                proceed.launch(intent)
            }
            else{
                setResult(441, intent)
                finish()
            }
        }
        car3button.setOnClickListener{
            reportProgress.putString("prediction",carMap[carLabels[2].toString()].toString())
            val intent = Intent(this, addVehicle::class.java)
            intent.putExtras(reportProgress)
            if(fromMain){
                proceed.launch(intent)
            }
            else{
                setResult(441, intent)
                finish()
            }
        }
    }
    private fun getImage(carName : String) : Uri? {
        val fileName = carName + ".jpg"
        toast("LOOKING FOR " + fileName)
        File("examples/").walk().forEach {
            if (it.name == fileName) return fromFile(it)
        }
        toast("Predicted car invalid")
        return fromFile(File(""))
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