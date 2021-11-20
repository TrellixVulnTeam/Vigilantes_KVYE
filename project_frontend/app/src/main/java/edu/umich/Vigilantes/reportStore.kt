package edu.umich.Vigilantes

import android.content.Context
import android.net.Uri
import android.telecom.Call
import android.widget.TextView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import kotlin.reflect.full.declaredMemberProperties
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.full.declaredMemberProperties
import androidx.appcompat.app.AppCompatActivity


object reportStore {

    private val serverUrl = "https://3.137.139.79/"

    private val client = OkHttpClient()

    val carLabels = arrayListOf<Int>()

    fun postImages(context: Context, imageUri: Uri?,
                  completion: (String) -> Unit?) {
        val mpFD = MultipartBody.Builder().setType(MultipartBody.FORM)
        imageUri?.run {
            toFile(context)?.let {
                mpFD.addFormDataPart("image", "carImage",
                    it.asRequestBody("image/jpeg".toMediaType()))
            } ?: context.toast("Unsupported image format")
        }

        val request = Request.Builder()
            .url(serverUrl+"postimages/")
            .post(mpFD.build())
            .build()

        context.toast("Posting . . .")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion(e.localizedMessage ?: "Posting failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    carLabels.clear()
                    val cars = try { JSONObject(response.body?.string() ?: "").getJSONArray("prediction") } catch (e: JSONException) { JSONArray() }
                    for(i in 0..2){
                        carLabels.add(cars[i] as Int)
                    }
                    completion("Added car labels")
                }
                else{
                    completion("Failed to predict car type... Is certificate installed?")
                }
            }
        })

    }


}