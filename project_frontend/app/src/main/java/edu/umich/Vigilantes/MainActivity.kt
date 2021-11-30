package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.view.View
import edu.umich.Vigilantes.databinding.ActivityMainBinding
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import edu.umich.Vigilantes.reportStoreVin.vin_predict
import edu.umich.Vigilantes.reportStoreLicense.lpn_predict
import edu.umich.Vigilantes.reportStoreLicense.postImagesLicense
import edu.umich.Vigilantes.reportStoreLicense.state_predict
import edu.umich.Vigilantes.reportStoreVin.postVin


class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding
    private var reportList: reportList = reportList()
    var reportBundle : Bundle? = Bundle()
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var forCropResult: ActivityResultLauncher<Intent>
    private var vinImageUri: Uri? = null
    private var carImageUri: Uri? = null
    private var plateImageUri: Uri? = null
    private var choice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ONCREATE", "onCreate for MainActivity")
        view = ActivityMainBinding.inflate(layoutInflater)
        forCropResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("CROPPING", "Cropping")
                    result.data?.data.let {
                        if (choice == 0) {
                            vinImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            vinImageUri = it
                            reportBundle?.putParcelable("vinImageUri", vinImageUri)
                            reportBundle?.putBoolean("check3",true)
                            reportBundle?.putParcelable("Report List",reportList)
                            getVin()

                        } else if (choice == 1) {
                            plateImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            plateImageUri = it
                            reportBundle?.putParcelable("plateImageUri", plateImageUri)
                            reportBundle?.putBoolean("check1",true)
                            reportBundle?.putParcelable("Report List",reportList)
                            getLicense()
                        } else {
                            carImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            carImageUri = it
                            reportBundle?.putParcelable("carImageUri", carImageUri)
                            reportBundle?.putBoolean("check2",true)
                            reportBundle?.putParcelable("Report List",reportList)
                            sendToResultsPage()
                        }
                    }
                } else {
                    Log.d("Crop", result.resultCode.toString())
                }
            }
        val cropIntent = initCropIntent()
        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture())
        { success ->
            if (success) {
                doCrop(cropIntent)
            } else {
                Log.d("TakePicture", "failed")
            }
        }
        setContentView(R.layout.activity_main)

        val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateandTime: String = sdf.format(Date())
        // val textView = findViewById<TextView>(R.id.date)
        // textView.text = currentDateandTime

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

        //Retrieve list of reports from sharedPreferences
        loadList()

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
        // var locationButton = findViewById<Button>(R.id.addLocationButton)


        //Listener button clicks
        carButton.setOnClickListener {
            choice = 2;
            carImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(carImageUri)

        }
        plateButton.setOnClickListener {
            choice = 1;
            plateImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(plateImageUri)
        }
        vinButton.setOnClickListener {
            choice = 0;
            vinImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(vinImageUri)
        }
        reportsButton.setOnClickListener {
            val intent = Intent(this, pastReports::class.java)
            intent.putExtra("Report List", reportList)
            startReport.launch(intent)
        }
//        locationButton.setOnClickListener {
//            val intent = Intent(this, addLoc::class.java)
//            startActivity(intent)
//        }
        debugButton.setOnClickListener {
            var report = reportObj()

            val intent = Intent(this, reportVehicleInfo::class.java)   //Change page to page being tested
            intent.putExtra("Report List", reportList)
            intent.putExtra("Report Info", report)
            startReport.launch(intent)
        }
    }

    private val startReport =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(it.resultCode == 441) {
                Log.d("debug message", "reports should be received")
                //If report is completed, retrieve latest report
                reportList = it.data?.getParcelableExtra("Report List")!!
                var report = it.data?.getParcelableExtra<reportObj>("Report Info")

                //Save report list
                saveList()

                //If new report exists, add to report list and go to preview
                if(report != null) {
                    goToPreview(reportList, report)
                }
            }
            else {
                Log.d("debug message", "Report List lost")
            }
        }

    private fun goToPreview(reportList: reportList, report: reportObj) {
        val intent = Intent(this, reportPreview::class.java)
        intent.putExtra("Report List", reportList)
        intent.putExtra("Report Info", report)
        startReport.launch(intent)
    }

    private fun saveList() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(reportList)
        editor.putString("list of reports", json)
        editor.apply()
    }

    private fun loadList() {
        val nullList = Gson().toJson(reportList())
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("list of reports", nullList)
        val type = object: TypeToken<reportList>(){}.type
        reportList = gson.fromJson(json, type)
    }
    private fun sendToResultsPage(){
        val reportIntent: Intent = Intent(this,recognizeActivity::class.java)
        reportBundle?.let{
        it.putBoolean("main",true)
        reportIntent.putExtras(it)}
        startReport.launch(reportIntent)
    }
    private fun getVin(){
        postVin(applicationContext, vinImageUri) { msg ->
            runOnUiThread {
                toast(msg)
                reportBundle?.putString("vin",vin_predict)
            }
        }
        val intent = Intent(this, addVehicle::class.java)
        intent.putExtras(reportBundle!!)
        startReport.launch(intent)
    }

    private fun getLicense(){
        postImagesLicense(applicationContext, plateImageUri) { msg ->
            runOnUiThread {
                toast(msg)
                reportBundle?.putString("license_plate", lpn_predict)
                reportBundle?.putString("state", state_predict)
            }
        }
        val intent = Intent(this, addVehicle::class.java)
        intent.putExtras(reportBundle!!)
        startReport.launch(intent)
    }
    private fun mediaStoreAlloc(mediaType: String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.MIME_TYPE, mediaType)
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

        return contentResolver.insert(
            if (mediaType.contains("video"))
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values)
    }

    private fun initCropIntent(): Intent? {
        // Is there any published Activity on device to do image cropping?
        val intent = Intent("com.android.camera.action.CROP")
        intent.type = "image/*"
        val listofCroppers = packageManager.queryIntentActivities(intent, 0)
        // No image cropping Activity published
        if (listofCroppers.size == 0) {
            toast("Device does not support image cropping")
            return null
        }

        intent.component = ComponentName(
            listofCroppers[0].activityInfo.packageName,
            listofCroppers[0].activityInfo.name)

        // create a square crop box:
        intent.putExtra("outputX", 500)
            .putExtra("outputY", 500)
            .putExtra("aspectX", 1)
            .putExtra("aspectY", 1)
            // enable zoom and crop
            .putExtra("scale", true)
            .putExtra("crop", true)
            .putExtra("return-data", true)

        return intent
    }

    private fun doCrop(intent: Intent?) {
        intent ?: run {
            //imageUri?.let { view.addCarImageButton.display(it) }
            return
        }

        if(choice == 0){
            vinImageUri?.let { // Vin
                intent.data = it
                forCropResult.launch(intent)
            }
        }
        else if(choice == 1){
            plateImageUri?.let { // Plate
                intent.data = it
                forCropResult.launch(intent)
            }
        }
        else{
            carImageUri?.let { // Plate
                intent.data = it
                forCropResult.launch(intent)
            }
        }

    }
}