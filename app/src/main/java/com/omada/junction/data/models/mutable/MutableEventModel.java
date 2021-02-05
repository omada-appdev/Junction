package com.omada.junction.data.models.mutable;

import com.google.common.collect.ImmutableList;
import com.omada.junction.data.models.external.EventModel;

import java.time.LocalDateTime;
import java.util.Map;


public class MutableEventModel extends EventModel {
    
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setForm(Map<String, Map<String, Map<String, String>>> form) {
        this.form = form;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setTimeCreated(LocalDateTime timeCreated){
        this.timeCreated = timeCreated;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setTags(ImmutableList<String> tags) {
        this.tags = tags;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public void setVenueInstitute(String venueInstitute) {
        this.venueInstitute = venueInstitute;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setCreatorProfilePicture(String creatorProfilePicture) {
        this.creatorProfilePicture = creatorProfilePicture;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public void setCreatorInstitute(String institute) {
        this.creatorInstitute = institute;
    }

}
