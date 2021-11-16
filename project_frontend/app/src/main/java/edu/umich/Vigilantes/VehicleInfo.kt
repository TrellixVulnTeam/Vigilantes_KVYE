package edu.umich.Vigilantes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleInfo(
    var position: Int? = 0,
    var makemodel: String? = null,
    var year: String? = null,
    var plateNumber: String? = null,
    var VIN: String? = null,
    var color: String? = null,
) : Parcelable