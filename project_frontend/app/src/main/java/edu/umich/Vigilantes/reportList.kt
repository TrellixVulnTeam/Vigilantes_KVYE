package edu.umich.Vigilantes

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
data class reportList(
    var reportList: MutableList<reportObj> = mutableListOf()
) : Parcelable {

    fun getList(): MutableList<reportObj> {
        return this.reportList
    }

    fun getLength(): Int {
        return this.reportList.size
    }

    fun find(report: reportObj): Int {
        for(r in reportList) {
            if(r.UID == report.UID) {
                Log.d("report found in index", r.getPos().toString())
                return r.getPos()
            }
        }
        Log.d("report not found","")
        return -1
    }

    fun addReport(report: reportObj) {
        var r = report
        r.setPos(getLength())
        Log.d("new report added at", r.getPos().toString())
        this.reportList.add(r)
    }

    fun editReport(report: reportObj) {
        this.reportList[report.getPos()] = report
    }

    fun deleteReport(report: reportObj) {
        this.reportList.removeAt(report.getPos())
        updatePositions()
    }

    private fun updatePositions() {
        for((index, report) in reportList.withIndex()) {
            report.setPos(index)
        }
    }
}
