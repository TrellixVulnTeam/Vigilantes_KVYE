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

    fun addReport(report: reportObj) {
        this.reportList.add(report)
    }

    fun deleteReport(position: Int) {
        this.reportList.removeAt(position)
    }
}
