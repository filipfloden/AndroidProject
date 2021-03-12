package se.ju.student.group16.androidproject

class Events(
    val eventID: String,
    val eventTitle: String,
    val eventDescription: String,
    val eventTheme: String,
    val eventDate: String,
    val eventLong: Double,
    val eventLat: Double,
    val eventHost: String
) {
    override fun toString() = eventTitle

}