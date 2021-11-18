package edu.umich.Vigilantes

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import edu.umich.Vigilantes.R
import android.graphics.BitmapFactory
import android.widget.Toast
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.os.Environment
import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Environment.getExternalStorageDirectory
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import edu.umich.Vigilantes.databinding.ActivityPdfBinding
import edu.umich.Vigilantes.pdfActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.util.Log

// A lot of the this activity was adapted from Geeks for Geeks
// Found at https://www.geeksforgeeks.org/how-to-generate-a-pdf-file-in-android-app/
// Credit to the author at https://auth.geeksforgeeks.org/user/chaitanyamunje/profile

class pdfActivity : AppCompatActivity() {
    private lateinit var view: ActivityPdfBinding

    // variables for our buttons.
    private lateinit var generatePDFbtn: Button

    // declaring width and height
    // for our PDF file.
    var pageHeight = 1120
    var pagewidth = 792

    // creating a bitmap variable
    // for storing our images
    var bmp: Bitmap? = null
    var scaledbmp: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_pdf)

        // initializing pdf button
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF)
        val report = intent.extras


        if (!checkPermission()) {
            requestPermission()
        }
        view.idBtnGeneratePDF.setOnClickListener { // genPDF
            generatePDF()
        }


    }

    fun buttonPDF(view: View) = generatePDF()


    fun generatePDF() {
        // Create PDF
        val pdfDocument = PdfDocument()
        val carReportTemplate =
            mutableListOf(mapOf("Name" to "Subaru", "LicensePlate" to "1234 ABC"),mapOf("Name" to "Hummer", "LicensePlate" to "123 ABC"))

        val paint = Paint()
        val title = Paint() // for title
        val report = Paint() // should be same for all reports

        // create title page
        val titlePageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()



        // below line is used for setting
        // start page for our PDF file.
        val titlePage = pdfDocument.startPage(titlePageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas = titlePage.canvas

        //canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint) // this is from GFG, used to make an image

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)


        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15f


        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(this, R.color.black)

        val dateTime = "December 12, 2021"
        val location = "Ann Arbor, MI"
        canvas.drawText(dateTime, 56f, 100f, title)
        canvas.drawText(location, 56f, 80f, title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.textSize = 18f
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("CAR REPORT", 396f, 560f, title)
        pdfDocument.finishPage(titlePage)

        val pages = mutableListOf<PdfDocument.PageInfo>()
        var number = 2
         // initialize page of reports
        for (car in carReportTemplate){
            pages.add(PageInfo.Builder(pagewidth, pageHeight, number).create())
            number += 1
        }
        report.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        report.textSize = 15f
        report.color = ContextCompat.getColor(this, R.color.black)
        var num = 0
        for(page in pages){
            val startedPage = pdfDocument.startPage(page)
            val reportCanvas = startedPage.canvas
            carReportTemplate[num]["Name"]?.let { reportCanvas.drawText(it,40f, 80f, report) }
            carReportTemplate[num]["LicensePlate"]?.let { reportCanvas.drawText(it,40f, 120f, report) }
            num += 1
            pdfDocument.finishPage(startedPage)
         }

        // below line is used to set the name of
        // our PDF file and its path.
        val documentDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString()
        val file = File(documentDir, "report.pdf")
        try {
            // Try writing pdf file to documents on phone
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this@pdfActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@pdfActivity, "Error generating PDF", Toast.LENGTH_SHORT)
                .show()
        }
        // Close the document
        pdfDocument.close()
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (!writeStorage || !readStorage) {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        // constant code for runtime permissions
        private const val PERMISSION_REQUEST_CODE = 200
    }
}