package edu.umich.Vigilantes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParticipantInfo(
    @Expose
    @SerializedName("participant position")
    var position: Int? = 0,
    @Expose
    @SerializedName("participant name")
    var name: String? = null,
    @Expose
    @SerializedName("participant addr")
    var addr: String? = null,
    @Expose
    @SerializedName("participant zip")
    var zip: String? = null,
    @Expose
    @SerializedName("participant city")
    var city: String? = null,
    @Expose
    @SerializedName("participant state")
    var state: String? = null,
    @Expose
    @SerializedName("participant license")
    var license: String? = null,
    @Expose
    @SerializedName("participant phone")
    var phone: String? = null,
    @Expose
    @SerializedName("participant insurance")
    var insurance: String? = null,
    @Expose
    @SerializedName("participant policy")
    var policy: String? = null,
    @Expose
    @SerializedName("participant expiration")
    var expiration: String? = null,
    @Expose
    @SerializedName("participant agent number")
    var agentNumber: String? = null
) : Parcelable