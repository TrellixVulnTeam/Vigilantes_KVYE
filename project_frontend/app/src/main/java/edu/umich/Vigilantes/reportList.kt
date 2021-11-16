package edu.umich.Vigilantes

import android.os.Parcelable
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
                return r.getPos()
            }
        }
        return -1
    }

    fun addReport(report: reportObj) {
        var r = report
        r.setPos(getLength())
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
