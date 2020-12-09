package com.omada.junction.data.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventModelRemoteDB extends BaseModel {

    private String id;
    private String title;
    private String description;
    private String image;

    private String creator;
    private HashMap<String, String> creatorCache;

    private Map <String, Map <String, Map <String, String>>> form;

    private String status;
    private Timestamp startTime;
    private Timestamp endTime;

    private String venue;
    private HashMap<String, String> venueCache;

    private ArrayList<String> tags;

    public EventModelRemoteDB(){
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("creator")
    public String getCreator() {
        return creator;
    }

    @PropertyName("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @PropertyName("creatorCache")
    public HashMap<String, String> getCreatorCache() {
        return creatorCache;
    }

    @PropertyName("creatorCache")
    public void setCreatorCache(HashMap<String, String> creatorCache) {
        this.creatorCache = creatorCache;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("form")
    public Map<String, Map<String, Map<String, String>>> getForm() {
        return form;
    }

    @PropertyName("form")
    public void setForm(Map<String, Map<String, Map<String, String>>> form) {
        this.form = form;
    }

    @PropertyName("image")
    public String getImage() {
        return image;
    }

    @PropertyName("image")
    public void setImage(String image) {
        this.image = image;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName("startTime")
    public Timestamp getStartTime() {
        return startTime;
    }

    @PropertyName("startTime")
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @PropertyName("endTime")
    public Timestamp getEndTime() {
        return endTime;
    }

    @PropertyName("endTime")
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @PropertyName("venue")
    public String getVenue() {
        return venue;
    }

    @PropertyName("venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @PropertyName("venueCache")
    public HashMap<String, String> getVenueCache() {
        return venueCache;
    }

    @PropertyName("venueCache")
    public void setVenueCache(HashMap<String, String> venueCache) {
        this.venueCache = venueCache;
    }

    @PropertyName("tags")
    public ArrayList<String> getTags() {
        return tags;
    }

    @PropertyName("tags")
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

}
