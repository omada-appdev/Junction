package com.omada.junction.data.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventModelRemoteDB extends BaseModel {

    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventPoster;

    private String eventOrganizer;
    private HashMap<String, String> eventOrganizerCache;

    private Map <String, Map <String, Map <String, String>>> eventForm;

    private String eventStatus;
    private Timestamp eventTimeStart;
    private Timestamp eventTimeEnd;

    private String eventVenue;
    private HashMap<String, String> eventVenueCache;

    private ArrayList<String> tags;
    private Integer usersRegistered;


    public EventModelRemoteDB(){
    }

    @PropertyName("eventName")
    public String getEventName() {
        return eventName;
    }

    @PropertyName("eventName")
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @PropertyName("eventOrganizer")
    public String getEventOrganizer() {
        return eventOrganizer;
    }

    @PropertyName("eventOrganizer")
    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    @PropertyName("eventOrganizerCache")
    public HashMap<String, String> getEventOrganizerCache() {
        return eventOrganizerCache;
    }

    @PropertyName("eventOrganizerCache")
    public void setEventOrganizerCache(HashMap<String, String> eventOrganizerCache) {
        this.eventOrganizerCache = eventOrganizerCache;
    }

    @PropertyName("eventDescription")
    public String getEventDescription() {
        return eventDescription;
    }

    @PropertyName("eventDescription")
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    @PropertyName("eventForm")
    public Map<String, Map<String, Map<String, String>>> getEventForm() {
        return eventForm;
    }

    @PropertyName("eventForm")
    public void setEventForm(Map<String, Map<String, Map<String, String>>> eventForm) {
        this.eventForm = eventForm;
    }

    @PropertyName("eventPoster")
    public String getEventPoster() {
        return eventPoster;
    }

    @PropertyName("eventPoster")
    public void setEventPoster(String eventPoster) {
        this.eventPoster = eventPoster;
    }

    @PropertyName("eventStatus")
    public String getEventStatus() {
        return eventStatus;
    }

    @PropertyName("eventStatus")
    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    @PropertyName("eventTimeStart")
    public Timestamp getEventTimeStart() {
        return eventTimeStart;
    }

    @PropertyName("eventTimeStart")
    public void setEventTimeStart(Timestamp eventTimeStart) {
        this.eventTimeStart = eventTimeStart;
    }

    @PropertyName("eventTimeEnd")
    public Timestamp getEventTimeEnd() {
        return eventTimeEnd;
    }

    @PropertyName("eventTimeEnd")
    public void setEventTimeEnd(Timestamp eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    @PropertyName("eventVenue")
    public String getEventVenue() {
        return eventVenue;
    }

    @PropertyName("eventVenue")
    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    @PropertyName("eventVenueCache")
    public HashMap<String, String> getEventVenueCache() {
        return eventVenueCache;
    }

    @PropertyName("eventVenueCache")
    public void setEventVenueCache(HashMap<String, String> eventVenueCache) {
        this.eventVenueCache = eventVenueCache;
    }

    @PropertyName("tags")
    public ArrayList<String> getTags() {
        return tags;
    }

    @PropertyName("tags")
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @PropertyName("usersRegistered")
    public Integer getUsersRegistered() {
        return usersRegistered;
    }

    @PropertyName("usersRegistered")
    public void setUsersRegistered(Integer usersRegistered) {
        this.usersRegistered = usersRegistered;
    }

    @Exclude
    public String getEventId() {
        return eventId;
    }

    @Exclude
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
