package com.omada.junction.data.models.internal.remote;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;
import com.omada.junction.data.models.internal.BaseModelInternal;

import java.util.Map;

public class BookingModelRemoteDB extends BaseModelInternal {

    private String venue;
    private Map<String, String> venueCache;

    private String event;
    private String eventName;
    private Timestamp timeCreated;

    private Timestamp startTime;
    private Timestamp endTime;

    private String creator;
    private Map<String, String> creatorCache;

    private String image;

    public BookingModelRemoteDB(){
    }

    public BookingModelRemoteDB(String id) {
        super(id);
    }

    @PropertyName("event")
    public String getEvent() {
        return event;
    }

    @PropertyName("name")
    public String getEventName() {
        return eventName;
    }

    @ServerTimestamp
    @PropertyName("timeCreated")
    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    @PropertyName("startTime")
    public Timestamp getStartTime() {
        return startTime;
    }

    @PropertyName("endTime")
    public Timestamp getEndTime() {
        return endTime;
    }

    @PropertyName("creator")
    public String getCreator() {
        return creator;
    }

    @PropertyName("creatorCache")
    public Map<String, String> getCreatorCache() {
        return creatorCache;
    }

    @PropertyName("image")
    public String getImage() {
        return image;
    }

    @PropertyName("venue")
    public String getVenue() {
        return venue;
    }

    @PropertyName("event")
    public void setEvent(String event) {
        this.event = event;
    }

    @PropertyName("name")
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @PropertyName("timeCreated")
    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @PropertyName("startTime")
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @PropertyName("endTime")
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @PropertyName("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @PropertyName("creatorCache")
    public void setCreatorCache(Map<String, String> creatorCache) {
        this.creatorCache = creatorCache;
    }

    @PropertyName("image")
    public void setImage(String image) {
        this.image = image;
    }

    @PropertyName("venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @PropertyName("venueCache")
    public Map<String, String> getVenueCache() {
        return venueCache;
    }

    @PropertyName("venueCache")
    public void setVenueCache(Map<String, String> venueCache) {
        this.venueCache = venueCache;
    }
}
