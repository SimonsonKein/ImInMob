package com.simon.project.model;

import java.io.Serializable;

public class EventInfo implements Serializable {
    String title;
    String eventID;

    public EventInfo( String eventID, String title) {
        this.title = title;
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public String getEventID() {
        return eventID;
    }
}
