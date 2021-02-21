package com.omada.junction.data.models.internal.remote;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;
import com.omada.junction.data.models.external.BaseModel;
import com.omada.junction.data.models.internal.BaseModelInternal;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventModelRemoteDB extends BaseModelInternal {

    private final String type = "event";

    private String title;
    private String description;
    private String image;

    private String creator;
    private Map<String, String> creatorCache;

    private Map <String, Map <String, Map<String, String>>> form;

    private String status;
    private Timestamp startTime;
    private Timestamp endTime;

    private Timestamp timeCreated;

    private String venue;
    private Map<String, String> venueCache;

    private List<String> tags;

    public EventModelRemoteDB(String id){
        super(id);
    }

    public EventModelRemoteDB(){
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("creator")
    public String getCreator() {
        return creator;
    }

    @PropertyName("creatorCache")
    public Map<String, String> getCreatorCache() {
        return creatorCache;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("form")
    public Map<String, Map<String, Map<String, String>>> getForm() {
        return form;
    }

    @PropertyName("image")
    public String getImage() {
        return image;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("startTime")
    public Timestamp getStartTime() {
        return startTime;
    }

    @PropertyName("endTime")
    public Timestamp getEndTime() {
        return endTime;
    }

    @PropertyName("venue")
    public String getVenue() {
        return venue;
    }

    @PropertyName("venueCache")
    public Map<String, String> getVenueCache() {
        return venueCache;
    }

    @PropertyName("tags")
    public List<String> getTags() {
        return tags;
    }

    @ServerTimestamp
    @PropertyName("timeCreated")
    public Timestamp getTimeCreated() {
        return timeCreated;
    }



    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @PropertyName("creatorCache")
    public void setCreatorCache(Map<String, String> creatorCache) {
        this.creatorCache = creatorCache;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("form")
    public void setForm(Map<String, Map<String, Map<String, String>>> form) {
        this.form = form;
    }

    @PropertyName("image")
    public void setImage(String image) {
        this.image = image;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName("startTime")
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @PropertyName("endTime")
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @PropertyName("venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @PropertyName("venueCache")
    public void setVenueCache(Map<String, String> venueCache) {
        this.venueCache = venueCache;
    }

    @PropertyName("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }
}