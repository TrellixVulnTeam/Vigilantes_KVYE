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

        // initializing our variables.
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF)
        scaledbmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_action_name), 140, 140, false)

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
        view.idBtnGeneratePDF.setOnClickListener { // calling method to
            // generate our PDF file.

            generatePDF()
        }


    }

    fun buttonPDF(view: View) = generatePDF()


     fun generatePDF() {
        // creating an object variable
        // for our PDF document.
        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val paint = Paint()
        val title = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage = pdfDocument.startPage(mypageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15f

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(this, R.color.purple_200)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("WE'RE CODING PROFESSIONALS", 209f, 100f, title)
        canvas.drawText("CAR", 209f, 80f, title)

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15f

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396f, 560f, title)

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
         val documentDir =
             Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                 .toString()
        val file = File(documentDir, "report.pdf")
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(this@pdfActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            // below line is used
            // to handle error
            e.printStackTrace()
            Toast.makeText(this@pdfActivity, "Bummer. It didn't work", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
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
        // requesting permissions if not provided.
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