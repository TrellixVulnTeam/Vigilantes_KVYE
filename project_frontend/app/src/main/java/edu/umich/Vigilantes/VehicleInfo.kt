package edu.umich.Vigilantes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleInfo(
    @Expose
    @SerializedName("vehicle position")
    var position: Int? = 0,
    @Expose
    @SerializedName("vehicle makemodel")
    var makemodel: String? = null,
    @Expose
    @SerializedName("vehicle year")
    var year: String? = null,
    @Expose
    @SerializedName("vehicle plate")
    var plateNumber: String? = null,
    @Expose
    @SerializedName("vehicle VIN")
    var VIN: String? = null,
    @Expose
    @SerializedName("vehicle color")
    var color: String? = null,
) : Parcelable