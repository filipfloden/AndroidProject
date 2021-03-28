package se.ju.student.group16.androidproject.event

import android.util.Log
import se.ju.student.group16.androidproject.firebaseRepository

val eventRepository = EventRepository().apply{}

class EventRepository {
    private val myEvents = mutableListOf<Events>()
    // upcomingEvents will contain both myEvents and the events the user is invited to
    private val upcomingEvents = mutableListOf<Events>()

    fun getAllMyEvents() = myEvents
    fun getAllUpcomingEvents() = upcomingEvents

    fun addMyEvent(eventID: String,
                   eventHost: String,
                   eventTitle: String,
                   eventDescription: String,
                   eventTheme: String,
                   eventDate: String,
                   eventLong: Double,
                   eventLat: Double,
                   guestList: Map<String, String>): Boolean {
        if (myEvents.add(
                        Events(
                                eventID,
                                eventHost,
                                eventTitle,
                                eventDescription,
                                eventTheme,
                                eventDate,
                                eventLong,
                                eventLat,
                                guestList)
                )
        ){
            Log.d("Added successfully", "brap")
            return true
        }else{
            Log.d("Added failed", "brap")
            return false
        }
    }
    fun addUpcomingEvent(eventID: String,
                          eventHost: String,
                          eventTitle: String,
                          eventDescription: String,
                          eventTheme: String,
                          eventDate: String,
                          eventLong: Double,
                          eventLat: Double,
                          guestList: Map<String, String>){
        upcomingEvents.add(
                Events(
                        eventID,
                        eventHost,
                        eventTitle,
                        eventDescription,
                        eventTheme,
                        eventDate,
                        eventLong,
                        eventLat,
                        guestList
                )
        )
    }

    fun getMyEventById(eventID: String) =
        myEvents.find {
            it.eventID == eventID
        }
    fun getUpcomingEventById(eventID: String) =
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
    fun updateMyEventById(eventID: String,
                          newEventTitle: String,
                          newEventDescription: String,
                          newEventTheme: String,
                          newEventDate: String,
                          newEventLong: Double,
                          newEventLat: Double,
                          newGuestList: Map<String, String>){
        getMyEventById(eventID)?.run {
            eventTitle = newEventTitle
            eventDescription = newEventDescription
            eventTheme = newEventTheme
            eventDate = newEventDate
            eventLong = newEventLong
            eventLat = newEventLat
            guestList = newGuestList
        }
    }
    fun updateUpcomingEvent(eventID: String,
                          newEventTitle: String,
                          newEventDescription: String,
                          newEventTheme: String,
                          newEventDate: String,
                          newEventLong: Double,
                          newEventLat: Double,
                          newGuestList: Map<String, String>){
        getUpcomingEventById(eventID)?.run {
            eventTitle = newEventTitle
            eventDescription = newEventDescription
            eventTheme = newEventTheme
            eventDate = newEventDate
            eventLong = newEventLong
            eventLat = newEventLat
            guestList = newGuestList
        }
    }
    fun clearEvents(){
        myEvents.clear()
        upcomingEvents.clear()
    }
}