package Pojo;

import java.util.ArrayList;

/**
 * Created by on 05/05/2017.
 */

public class EventsServicePojo {

    String eventId;


    String event_type;
    ArrayList<EventTypesPojo> events;

    public EventsServicePojo(String eventId, String event_type, ArrayList<EventTypesPojo> events) {
        this.eventId = eventId;
        this.event_type = event_type;
        this.events = events;
    }

    public EventsServicePojo() {

    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public ArrayList<EventTypesPojo> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventTypesPojo> events) {
        this.events = events;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
