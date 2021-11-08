package edu.umich.Vigilantes

import android.app.Activity
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isNotEmpty
import edu.umich.Vigilantes.databinding.ActivityAddvehicleBinding
import java.net.URI

class addVehicle : AppCompatActivity() {
    private lateinit var view: ActivityAddvehicleBinding
    private lateinit var forCropResult: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var popupExists = false
    private var imageUri: Uri? = null
    private var carImageUri: Uri? = null
    private var plateImageUri: Uri? = null
    private var choice: Int = 0
    var report : Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ONCREATE", "onCreate for addVehicle")
        setContentView(R.layout.activity_addvehicle)

        view = ActivityAddvehicleBinding.inflate(layoutInflater)

        forCropResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("CROPPING", "Cropping")
                    result.data?.data.let {
                        if(choice == 0){
                            imageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            imageUri = it
                            report.putParcelable("vin",imageUri)
                        }
                        else if (choice == 1){
                            plateImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            plateImageUri = it
                            report.putParcelable("plate",plateImageUri)
                        }
                        else{
                            carImageUri?.run {
                                if (!toString().contains("ORIGINAL")) {
                                    // delete uncropped photo taken for posting
                                    contentResolver.delete(this, null, null)
                                }
                            }
                            carImageUri = it
                            report.putParcelable("car",carImageUri)
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

        }
        plateButton.setOnClickListener {
            choice = 1;
            plateImageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(plateImageUri)
            plateButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
        }
        vinButton.setOnClickListener {
            choice = 0;
            imageUri = mediaStoreAlloc("image/jpeg")
            takePicture.launch(imageUri)
            vinButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.checkbox_on_background,0)
        }
        continueButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)   //Change page to page being tested
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

    /*private fun createPopUp(picType : Int) {
        //Initialize popup window
        Log.d("createPopUp", "Calling createPopUp")
        val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.photo_popup_box, null)
        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        //Set popup window on top of parent view
        popupWindow.elevation = 10.0F

        val slideIn = Slide()
        slideIn.slideEdge = Gravity.START
        popupWindow.exitTransition = slideIn

        val slideOut = Slide()
        slideOut.slideEdge = Gravity.RIGHT
        popupWindow.exitTransition = slideOut

        val cameraOpt = view.findViewById<Button>(R.id.CameraOption)
        val galleryOpt = view.findViewById<Button>(R.id.GalleryOption)
        //Camera option chosen
        cameraOpt.setOnClickListener{
            //Implement camera
            Log.d("CAMERA", "Camera chosen")
            imageUri = mediaStoreAlloc("image/jpeg") // Do something here?
            takePicture.launch(imageUri)
            when(picType) {
                1 -> {

                }
                2 -> {

                }
                3 -> {

                }
            }
            //Dismiss popup window after
            popupWindow.dismiss()
            popupExists = false
        }
        //Gallery option chosen
        galleryOpt.setOnClickListener{
            //Implement gallery

            when(picType) {
                1 -> {

                }
                2 -> {

                }
                3 -> {

                }
            }
            //Dismiss popup window after
            popupWindow.dismiss()
            popupExists = false
        }

        //Display popup window
        var root_layout = findViewById<ConstraintLayout>(R.id.root_layout)
        popupWindow.showAtLocation(root_layout, Gravity.CENTER, 0, 0)
    }*/

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
            imageUri?.let { // Vin
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
    fun sendToPDF(){
        val reportIntent: Intent = Intent(this,pdfActivity::class.java)
        reportIntent.putExtras(report)
        startActivity(reportIntent)
    }
    fun sendToResultsPage(){
        val reportIntent: Intent = Intent(this,recognizeActivity::class.java)
        reportIntent.putExtras(report)
        startActivity(reportIntent)
    }
}