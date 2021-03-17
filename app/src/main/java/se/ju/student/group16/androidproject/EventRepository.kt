package se.ju.student.group16.androidproject

val eventRepository = EventRepository().apply{}

class EventRepository {
    private val myEvents = mutableListOf<Events>()
    // upcomingEvents will contain both myEvents and the events the user is invited to
    private val upcomingEvents = mutableListOf<Events>()

    fun getAllMyEvents() = myEvents
    fun getAllUpcomingEvents() = upcomingEvents

    fun addMyEvent(eventID: String, eventTitle: String, eventDescription: String, eventTheme: String
                   , eventDate: String, eventLong: Double, eventLat: Double, eventHost: String
                   , guestList: Map<User, String>){
        myEvents.add(
                Events(
                        eventID,
                        eventTitle,
                        eventDescription,
                        eventTheme,
                        eventDate,
                        eventLong,
                        eventLat,
                        eventHost,
                        guestList
                )
        )
    }
    fun addUpcomingEvents(eventID: String, eventTitle: String, eventDescription: String, eventTheme: String
                   , eventDate: String, eventLong: Double, eventLat: Double, eventHost: String
                   , guestList: Map<User, String>){
        upcomingEvents.add(
                Events(
                        eventID,
                        eventTitle,
                        eventDescription,
                        eventTheme,
                        eventDate,
                        eventLong,
                        eventLat,
                        eventHost,
                        guestList
                )
        )
    }

    fun getEventById(eventID: String) =
        upcomingEvents.find {
            it.eventID == eventID
        }
    fun deleteMyEventById(eventID: String) =
        myEvents.remove(
            myEvents.find{
                it.eventID == eventID
            }
        )
    fun deleteUpcomingEventById(eventID: String) =
            upcomingEvents.remove(
                    upcomingEvents.find{
                        it.eventID == eventID
                    }
            )
    fun clearEvents(){
        myEvents.clear()
        upcomingEvents.clear()
    }
}