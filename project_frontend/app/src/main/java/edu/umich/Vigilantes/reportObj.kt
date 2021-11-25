package edu.umich.Vigilantes

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class reportObj(
    @Expose
    @SerializedName("UID")
    var UID: UUID = UUID.randomUUID(),
    @Expose
    @SerializedName("position")
    var position: Int = 0,
    @Expose
    @SerializedName("datetime")
    var datetime: String? = "",
    @Expose
    @SerializedName("location")
    var location: String? = "",
    @Expose
    @SerializedName("incidentDesc")
    var incidentDesc: String? = "",
    @Expose
    @SerializedName("list of vehicles")
    var vehicleList: MutableList<VehicleInfo> = mutableListOf(),
    @Expose
    @SerializedName("list of participants")
    var participantList: MutableList<ParticipantInfo> = mutableListOf(),
    @Expose
    @SerializedName("list of witnesses")
    var witnessList: MutableList<WitnessInfo> = mutableListOf()
) : Parcelable {
    fun updateTo(report: reportObj) {
        this.setPos(report.getPos())
        report.getDateTime()?.let { this.setDateTime(it) }
        report.getLoc()?.let { this.setLoc(it) }
        report.getDesc()?.let { this.setDesc(it) }
        setVehicles(report.getVehicles())
        setParticipants(report.getParticipants())
        setWitnesses(report.getWitnesses())

        Log.d("reportObj", "report updated")
    }

    fun setPos(position: Int) {
        this.position = position
    }

    fun getPos(): Int {
        Log.d("This report is at position ", this.position.toString())
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