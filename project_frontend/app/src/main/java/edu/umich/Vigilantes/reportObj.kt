package edu.umich.Vigilantes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class reportObj(
    var position: Int = 0,
    var datetime: String? = "",
    var location: String? = "",
    var incidentDesc: String? = "",
    var vehicleList: MutableList<VehicleInfo> = mutableListOf(),
    var participantList: MutableList<ParticipantInfo> = mutableListOf(),
    var witnessList: MutableList<WitnessInfo> = mutableListOf()
) : Parcelable {
    fun setDateTime(datetime: String) {
        this.datetime = datetime
    }

    fun getDateTime(): String? {
        return datetime
    }

    fun setLoc(location: String) {
        this.location = location
    }

    fun getLoc(): String? {
        return location
    }

    fun setDesc(incidentDesc: String) {
        this.incidentDesc = incidentDesc
    }

    fun getDesc(): String? {
        return incidentDesc
    }

    fun setVehicles(vehicleList: MutableList<VehicleInfo>) {
        this.vehicleList = vehicleList
    }

    fun getVehicles(): MutableList<VehicleInfo> {
        return vehicleList
    }

    fun setParticipants(participantList: MutableList<ParticipantInfo>) {
        this.participantList = participantList
    }

    fun getParticipants(): MutableList<ParticipantInfo> {
        return participantList
    }

    fun setWitnesses(witnessList: MutableList<WitnessInfo>) {
        this.witnessList = witnessList
    }

    fun getWitnesses(): MutableList<WitnessInfo> {
        return witnessList
    }
}