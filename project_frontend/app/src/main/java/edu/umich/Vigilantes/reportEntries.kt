package edu.umich.Vigilantes

data class reportEntries (
    var reportID: Int? = 0
) {
    //List of participants involved in report
    var reportParticipants: MutableList<ParticipantInfo> = mutableListOf()

    //Add participant to list
    private fun addParticipantToReport(participant: ParticipantInfo) {
        reportParticipants.add(participant)
    }
}