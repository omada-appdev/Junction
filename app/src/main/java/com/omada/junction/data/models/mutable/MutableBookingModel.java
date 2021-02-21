package com.omada.junction.data.models.mutable;

import com.google.firebase.Timestamp;
import com.omada.junction.data.models.external.BookingModel;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.utils.TransformUtilities;


public class MutableBookingModel extends BookingModel {

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setCreatorProfilePicture(String creatorProfilePicture) {
        this.creatorProfilePicture = creatorProfilePicture;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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


    public static MutableBookingModel fromEventModel(EventModel eventModel) {

        MutableBookingModel mutableBookingModel = new MutableBookingModel();

        mutableBookingModel.setVenue(eventModel.getVenue());
        mutableBookingModel.setVenueName(eventModel.getVenueName());
        mutableBookingModel.setVenueAddress(eventModel.getVenueAddress());
        mutableBookingModel.setVenueInstitute(eventModel.getVenueInstitute());
        mutableBookingModel.setEvent(eventModel.getId());
        mutableBookingModel.setEventName(eventModel.getTitle());

        // TODO ServerTimestamp
        //mutableBookingModel.setTimeCreated(new Timestamp(eventModel.getTimeCreated()));

        mutableBookingModel.setStartTime(TransformUtilities.convertUtcLocalDateTimeToTimestamp(eventModel.getStartTime()));
        mutableBookingModel.setEndTime(TransformUtilities.convertUtcLocalDateTimeToTimestamp(eventModel.getEndTime()));
        mutableBookingModel.setCreator(eventModel.getCreator());
        mutableBookingModel.setCreatorName(eventModel.getCreatorName());
        mutableBookingModel.setCreatorProfilePicture(eventModel.getCreatorProfilePicture());
        mutableBookingModel.setCreatorMail(eventModel.getCreatorMail());
        mutableBookingModel.setCreatorPhone(eventModel.getCreatorPhone());

        return mutableBookingModel;

    }

}
