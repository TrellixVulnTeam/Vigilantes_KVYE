package edu.umich.Vigilantes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isNotEmpty

class addVehicle : AppCompatActivity() {
    private var popupExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addvehicle)

        //Find buttons
        var carButton = findViewById<ImageView>(R.id.addCarImageButton)
        var plateButton = findViewById<ImageView>(R.id.addPlateImageButton)
        var vinButton = findViewById<ImageView>(R.id.addVINImageButton)
        var continueButton = findViewById<Button>(R.id.ContinueButton)

        //Listener for clicks
        carButton.setOnClickListener {
            if(!popupExists) {
                createPopUp(1)
                popupExists = true
            }
        }
        plateButton.setOnClickListener {
            if(!popupExists) {
                createPopUp(2)
                popupExists = true
            }
        }
        vinButton.setOnClickListener {
            if(!popupExists) {
                createPopUp(3)
                popupExists = true
            }
        }
        continueButton.setOnClickListener {

        }
    }

    private fun createPopUp(picType : Int) {
        //Initialize popup window
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
    }
}