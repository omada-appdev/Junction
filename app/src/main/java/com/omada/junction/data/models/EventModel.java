package com.omada.junction.data.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.Nonnull;

public class EventModel extends BaseModel {

    private String eventId;
    private String eventName;
    private String eventDescription;

    private String eventOrganizer;
    private String eventOrganizerName;
    private String eventOrganizerType;
    private String eventOrganizerPhone;
    private String eventOrganizerMail;

    private String eventOrganizerProfilePictureRemote;
    private String eventOrganizerPhotoLocal;

    private Map<String, Map<String, Map <String, String>>> eventForm;

    private String eventPosterRemote;
    private String eventPosterLocal;

    private String eventStatus;
    private Date eventTimeStart;
    private Date eventTimeEnd;

    private String eventVenue;
    private String eventVenueName;
    private String eventVenueDetails;

    private ArrayList<String> tags;
    private Integer usersRegistered;


    public EventModel(@Nonnull EventModelRemoteDB modelRemoteDB){

        setEventId(modelRemoteDB.getEventId());
        setEventName(modelRemoteDB.getEventName());
        setEventDescription(modelRemoteDB.getEventDescription());

        setEventPosterRemote(modelRemoteDB.getEventPoster());

        setEventOrganizer(modelRemoteDB.getEventOrganizer());
        setEventOrganizerName(modelRemoteDB.getEventOrganizerCache().get("name"));
        setEventOrganizerPhone(modelRemoteDB.getEventOrganizerCache().get("phone"));
        setEventOrganizerProfilePictureRemote(modelRemoteDB.getEventOrganizerCache().get("profilePicture"));
        setEventOrganizerMail(modelRemoteDB.getEventOrganizerCache().get("mail"));

        setEventForm(modelRemoteDB.getEventForm());

        setEventStatus(modelRemoteDB.getEventStatus());
        setEventTimeStart(modelRemoteDB.getEventTimeStart());
        setEventTimeEnd(modelRemoteDB.getEventTimeEnd());

        setEventVenue(modelRemoteDB.getEventVenue());
        setEventVenueName(modelRemoteDB.getEventVenueCache().get("name"));
        setEventVenueDetails(modelRemoteDB.getEventVenueCache().get("details"));

        setTags(modelRemoteDB.getTags());
        setUsersRegistered(modelRemoteDB.getUsersRegistered());
    }


    public EventModel(EventModelLocalDB modelLocalDB){
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Map <String, Map<String, Map<String, String>>> getEventForm() {
        return eventForm;
    }

    private void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public Timestamp getEventTimeStart() {
        return new Timestamp(eventTimeStart);
    }

    public Timestamp getEventTimeEnd() {
        return new Timestamp(eventTimeEnd);
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public Integer getUsersRegistered() {
        return usersRegistered;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventVenueName() {
        return eventVenueName;
    }

    public String getEventVenueDetails() {
        return eventVenueDetails;
    }

    public String getEventOrganizerName() {
        return eventOrganizerName;
    }

    public String getEventOrganizerType() {
        return eventOrganizerType;
    }

    public String getEventOrganizerProfilePictureRemote() {
        return eventOrganizerProfilePictureRemote;
    }

    public String getEventPosterRemote() {
        return eventPosterRemote;
    }

    public String getEventPosterLocal() {
        return eventPosterLocal;
    }

    public String getEventOrganizerPhotoLocal() {
        return eventOrganizerPhotoLocal;
    }

    public String getEventOrganizerPhone() {
        return eventOrganizerPhone;
    }



    private void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    private void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    private void setEventForm(Map<String, Map<String, Map<String, String>>> eventForm) {
        this.eventForm = eventForm;
    }

    private void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    private void setEventTimeStart(Timestamp eventTimeStart) {
        this.eventTimeStart = eventTimeStart.toDate();
    }

    private void setEventTimeEnd(Timestamp eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd.toDate();
    }

    private void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    private void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    private void setUsersRegistered(Integer usersRegistered) {
        this.usersRegistered = usersRegistered;
    }

    private void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private void setEventVenueName(String eventVenueName) {
        this.eventVenueName = eventVenueName;
    }

    private void setEventVenueDetails(String eventVenueDetails) {
        this.eventVenueDetails = eventVenueDetails;
    }

    private void setEventOrganizerName(String eventOrganizerName) {
        this.eventOrganizerName = eventOrganizerName;
    }

    private void setEventOrganizerType(String eventOrganizerType) {
        this.eventOrganizerType = eventOrganizerType;
    }

    private void setEventOrganizerProfilePictureRemote(String eventOrganizerProfilePictureRemote) {
        this.eventOrganizerProfilePictureRemote = eventOrganizerProfilePictureRemote;
    }

    private void setEventPosterRemote(String eventPosterRemote) {
        this.eventPosterRemote = eventPosterRemote;
    }

    private void setEventPosterLocal(String eventPosterLocal) {
        this.eventPosterLocal = eventPosterLocal;
    }

    private void setEventOrganizerPhotoLocal(String eventOrganizerPhotoLocal) {
        this.eventOrganizerPhotoLocal = eventOrganizerPhotoLocal;
    }

    public String getEventOrganizerMail() {
        return eventOrganizerMail;
    }

    private void setEventOrganizerPhone(String eventOrganizerPhone) {
        this.eventOrganizerPhone = eventOrganizerPhone;
    }

    private void setEventOrganizerMail(String eventOrganizerMail) {
        this.eventOrganizerMail = eventOrganizerMail;
    }
}
