package edu.umich.Vigilantes

import android.content.Context
import android.net.Uri
import android.telecom.Call
import android.util.Log
import androidx.core.net.toFile
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.reflect.full.declaredMemberProperties


object reportStore {

    private val serverUrl = "https://3.137.139.79/"

    private val client = OkHttpClient()

    fun postImages(context: Context, imageUri: Uri?,
                  completion: (String) -> Unit) {
        val mpFD = MultipartBody.Builder().setType(MultipartBody.FORM)
        imageUri?.run {
            toFile()?.let {
                mpFD.addFormDataPart("image", "image",
                    it.asRequestBody("image/jpeg".toMediaType()))
            }
        }

        val request = Request.Builder()
            .url(serverUrl+"postimages/")
            .post(mpFD.build())
            .build()

        context.toast("Posting . . . wait for 'Chatt posted!'")


        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            println(response.body!!.string())
        }
    }

}