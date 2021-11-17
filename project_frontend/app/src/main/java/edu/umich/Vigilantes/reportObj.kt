package edu.umich.Vigilantes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class reportObj(
    var UID: UUID = UUID.randomUUID(),
    var position: Int = 0,
    var datetime: String? = "",
    var location: String? = "",
    var incidentDesc: String? = "",
    var vehicleList: MutableList<VehicleInfo> = mutableListOf(),
    var participantList: MutableList<ParticipantInfo> = mutableListOf(),
    var witnessList: MutableList<WitnessInfo> = mutableListOf()
) : Parcelable {
    fun setPos(position: Int) {
        this.position = position
    }

    fun getPos(): Int {
        return this.position
    }

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