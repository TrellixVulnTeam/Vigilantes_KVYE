package edu.umich.Vigilantes

data class ParticipantInfo(
    var name: String? = null,
    var addr: String? = null,
    var zip: String? = null,
    var city: String? = null,
    var state: String? = null,
    var license: String? = null,
    var insurance: String? = null,
    var policy: String? = null,
    var expiration: String? = null,
    var agentNumber: String? = null
)