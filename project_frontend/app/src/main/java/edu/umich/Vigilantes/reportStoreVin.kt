package edu.umich.Vigilantes

import android.content.Context
import android.net.Uri
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object reportStoreVin {
    private val serverUrl = "https://3.137.139.79/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    var vin_predict = ""

    fun postVin(context: Context, imageUri: Uri?,
                          completion: (String) -> Unit?) {
        val mpFD = MultipartBody.Builder().setType(MultipartBody.FORM)
        imageUri?.run {
            toFile(context)?.let {
                mpFD.addFormDataPart("image", "carImage",
                    it.asRequestBody("image/jpeg".toMediaType()))
            } ?: context.toast("Unsupported image format")
        }

        val request = Request.Builder()
            .url(serverUrl+"postvin/")
            .post(mpFD.build())
            .build()

        context.toast("Posting . . .")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                completion(e.localizedMessage ?: "Posting failed")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    val result = try { JSONObject(response.body?.string() ?: "") } catch (e: JSONException) { JSONObject() }
                    vin_predict = result["vin"] as String
                    completion("Added vin")
                }
                else{
                    completion("Failed to extract license plate... Is certificate installed?")
                }
            }
        })

    }
}