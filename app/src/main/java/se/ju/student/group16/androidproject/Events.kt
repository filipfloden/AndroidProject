package se.ju.student.group16.androidproject

data class Events(
        val eventID: String,
        val eventHost: String,
        var eventTitle: String,
        var eventDescription: String,
        var eventTheme: String,
        var eventDate: String,
        var eventLong: Double,
        var eventLat: Double,
        var guestList: Map<String, String>
) {
    override fun toString() = eventTitle
}