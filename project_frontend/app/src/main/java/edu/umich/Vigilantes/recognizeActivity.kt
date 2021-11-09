package edu.umich.Vigilantes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umich.Vigilantes.reportStore.postImages
import android.content.Context
import kotlinx.serialization.json.*
import com.google.gson.*
import edu.umich.Vigilantes.databinding.ActivityRecognizeBinding
import android.widget.*

class recognizeActivity : AppCompatActivity() {
    private lateinit var view: ActivityRecognizeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognize)
        view = ActivityRecognizeBinding.inflate(layoutInflater)
        val jsonString = applicationContext.assets.open("easy_labels.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        var map: Map<String, Any> = HashMap()
        val carMap = gson.fromJson(jsonString,map.javaClass)
        val reportProgress: Bundle = intent.extras!!
        val uri: Uri? = reportProgress.getParcelable("car")!!
        postImages(applicationContext,uri) { msg ->
            runOnUiThread {
                toast(msg)
            }
            finish()
        }

    }
}