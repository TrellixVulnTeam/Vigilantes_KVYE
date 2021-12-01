package edu.umich.Vigilantes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WitnessInfo(
    @Expose
    @SerializedName("witness position")
    var position: Int? = 0,
    @Expose
    @SerializedName("witness name")
    var name: String? = null,
    @Expose
    @SerializedName("witness phone")
    var phone: String? = null,
    @Expose
    @SerializedName("witness desc")
    var description: String? = null
) : Parcelable