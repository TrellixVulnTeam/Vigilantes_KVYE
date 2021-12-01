package edu.umich.Vigilantes

import android.app.Activity
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.umich.Vigilantes.databinding.ActivityMoreVehiclesBinding

class moreVehicles : AppCompatActivity() {
    // Looks a lot like addvehicle
    // That's because it is
    // Passing intents through addvehicle in too many directions was getting way too hard
    private lateinit var view: ActivityMoreVehiclesBinding
    private lateinit var forCropResult: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var vinImageUri: Uri? = null
    private var carImageUri: Uri? = null
    private var plateImageUri: Uri? = null
    private var choice: Int = 0
    private var check1: Boolean = false
    private var check2: Boolean = false
    private var check3: Boolean = false
    var report : Bundle? = Bundle()
    var currentReport : reportObj = reportObj()
    var reports: reportList = reportList()
    var finishedCar: VehicleInfo = VehicleInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeVisuals(intent)
        Log.d("ONCREATE", "onCreate for addVehicle")
        setContentView(R.layout.activity_addvehicle)

        view = ActivityMoreVehiclesBinding.inflate(layoutInflater)

        forCropResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("CROPPING", "Cropping")
                    result.data?.data.let {
                        if(choice == 0){
                            vinImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            vinImageUri = it
                            report?.putParcelable("vinImageUri",vinImageUri)
                        }
                        else if (choice == 1){
                            plateImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            plateImageUri = it
                            report?.putParcelable("plateImageUri",plateImageUri)
                        }
                        else{
                            carImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            carImageUri = it
                            report?.putParcelable("carImageUri",carImageUri)
                            sendToResultsPage()
                        }

                        //imageUri?.let { view.addCarImageButton.display(it) } // TODO: Generalize
                    }
                } else {
                    Log.d("Crop", result.resultCode.toString())
                }
            }
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            toast("Device has no camera!")
            return
        }
        val cropIntent = initCropIntent()
        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture())
        { success ->
            if (success) {
                doCrop(cropIntent)
            } else {
                Log.d("TakePicture", "failed")
            }
        } // CHECK
        //Find buttons
        var carButton = findViewById<Button>(R.id.addCarImageButton)
        var plateButton = findViewById<Button>(R.id.addPlateImageButton)
        var vinButton = findViewById<Button>(R.id.addVINImageButton)
        var continueButton = findViewById<Button>(R.id.ContinueButton)

        //Listener for clicks
        carButton.setOnClickListener {
            Log.d("CARBUTTON", "carButton clicked")
            choice = 2;
            carImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(carImageUri)
            carButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
            check2 = true
            report?.putBoolean("check2",check2)
        }
        plateButton.setOnClickListener {
            choice = 1;
            plateImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(plateImageUri)
            plateButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
            check1 = true
            report?.putBoolean("check1",check1)
        }
        vinButton.setOnClickListener {
            choice = 0;
            vinImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(vinImageUri)
            check3 = true;
            report?.putBoolean("check3",check3)
            vinButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
        }
        continueButton.setOnClickListener {
            val intent = Intent(this, reportVehicleInfo::class.java)   //Change page to page being tested
            // Need to add a vehicle to the non-null report

            startActivity(intent)
        }
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
    } // From EECS 441 Lab 1

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

    } // Ditto
    private fun sendToResultsPage(){
        val reportIntent: Intent = Intent(this,recognizeActivity::class.java)
        report?.let{reportIntent.putExtras(it)}
        startActivity(reportIntent)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelable("carImageUri", carImageUri)
        savedInstanceState.putBoolean("check1",check1)
        savedInstanceState.putBoolean("check2",check2)
        savedInstanceState.putBoolean("check3",check3)

    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        carImageUri = savedInstanceState.getParcelable<Uri>("carImageUri")

        check1 = savedInstanceState.getBoolean("check1",check1)
        check2 = savedInstanceState.getBoolean("check2",check2)
        check3 = savedInstanceState.getBoolean("check3",check3)
        var carButton = findViewById<Button>(R.id.addCarImageButton)
        var plateButton = findViewById<Button>(R.id.addPlateImageButton)
        var vinButton = findViewById<Button>(R.id.addVINImageButton)
        if(check1){
            plateButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
        }
        if(check2){
            carButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
        }
        if(check3){
            vinButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
        }
    }

    fun changeVisuals(intent: Intent?){
        intent?.let{
            intent.extras?.let{
                report = intent.extras
            }
            report?.let{
                carImageUri = it.getParcelable("carImageUri")
                vinImageUri = it.getParcelable("vinImageUri")
                plateImageUri = it.getParcelable("plateImageUri")
                check1 = it.getBoolean("check1",check1)
                check2 = it.getBoolean("check2",check2)
                check3 = it.getBoolean("check3",check3)
            }
            var carButton = findViewById<Button>(R.id.addCarImageButton)
            var plateButton = findViewById<Button>(R.id.addPlateImageButton)
            var vinButton = findViewById<Button>(R.id.addVINImageButton)
            if(check1){
                plateButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
            }
            if(check2){
                carButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
            }
            if(check3){
                vinButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
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
                val prediction: String? = report?.getString("prediction")
                prediction?.let{
                    val words = prediction.split(" ".toRegex())
                    val year: String = words[words.size-1]
                    var makeModel: String = ""
                    for(i in 0..words.size-2){
                        makeModel += words[i]
                    }
                    finishedCar.makemodel = makeModel
                    finishedCar.year = year
                }
                report?.getString("vin")?.let{
                    finishedCar.VIN = it
                }
                report?.getString("plateNumber")?.let{
                    finishedCar.plateNumber = it
                }
                var passableReport: reportObj = reportObj()
                passableReport.vehicleList = mutableListOf()
                passableReport.vehicleList.add(finishedCar)

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