package com.omada.junction.data.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.Nonnull;

public class EventModel extends BaseModel {

    private String id;
    private String title;
    private String description;

    private String creator;
    private String creatorName;
    private String creatorPhone;
    private String creatorMail;

    private String creatorProfilePicture;

    private Map<String, Map<String, Map <String, String>>> form;

    private String image;

    private String status;
    private Date startTime;
    private Date endTime;

    private String venue;
    private String venueName;
    private String venueAddress;

    private ArrayList<String> tags;


    public EventModel(@Nonnull EventModelRemoteDB modelRemoteDB){

        setId(modelRemoteDB.getId());
        setTitle(modelRemoteDB.getTitle());
        setDescription(modelRemoteDB.getDescription());

        setImage(modelRemoteDB.getImage());

        setCreator(modelRemoteDB.getCreator());
        setCreatorName(modelRemoteDB.getCreatorCache().get("name"));
        setCreatorPhone(modelRemoteDB.getCreatorCache().get("phone"));
        setCreatorProfilePicture(modelRemoteDB.getCreatorCache().get("profilePicture"));
        setCreatorMail(modelRemoteDB.getCreatorCache().get("mail"));

        setForm(modelRemoteDB.getForm());

        setStatus(modelRemoteDB.getStatus());
        setStartTime(modelRemoteDB.getStartTime());
        setEndTime(modelRemoteDB.getEndTime());

        setVenue(modelRemoteDB.getVenue());
        setVenueName(modelRemoteDB.getVenueCache().get("name"));
        setVenueAddress(modelRemoteDB.getVenueCache().get("address"));

        setTags(modelRemoteDB.getTags());
    }

    public EventModel(EventModelLocalDB modelLocalDB){
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public Map <String, Map<String, Map<String, String>>> getForm() {
        return form;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getStartTime() {
        return new Timestamp(startTime);
    }

    public Timestamp getEndTime() {
        return new Timestamp(endTime);
    }

    public String getVenue() {
        return venue;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorProfilePicture() {
        return creatorProfilePicture;
    }

    public String getImage() {
        return image;
    }

    public String getCreatorPhone() {
        return creatorPhone;
    }



    private void setCreator(String creator) {
        this.creator = creator;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setForm(Map<String, Map<String, Map<String, String>>> form) {
        this.form = form;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    private void setStartTime(Timestamp startTime) {
        this.startTime = startTime.toDate();
    }

    private void setEndTime(Timestamp endTime) {
        this.endTime = endTime.toDate();
    }

    private void setVenue(String venue) {
        this.venue = venue;
    }

    private void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    private void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    private void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    private void setCreatorProfilePicture(String creatorProfilePicture) {
        this.creatorProfilePicture = creatorProfilePicture;
    }

    private void setImage(String image) {
        this.image = image;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    private void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }

    private void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }
}
