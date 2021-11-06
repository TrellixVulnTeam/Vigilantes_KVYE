package edu.umich.Vigilantes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WitnessInfo(
    var position: Int? = 0,
    var name: String? = null,
    var phone: String? = null,
    var description: String? = null
) : Parcelable