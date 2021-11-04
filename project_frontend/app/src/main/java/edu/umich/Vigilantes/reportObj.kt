package edu.umich.Vigilantes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class reportObj(
    var position: Int = 0,
    var datetime: String? = "",
    var location: String? = "",
    var vehicleList: MutableList<VehicleInfo> = mutableListOf(),
    var participantList: MutableList<ParticipantInfo> = mutableListOf(),
    var witnessList: MutableList<WitnessInfo> = mutableListOf()
) : Parcelable