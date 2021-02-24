package com.omada.junction.data.models.external;

import android.os.Parcel;

import com.google.common.collect.ImmutableList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;


public class EventModel extends PostModel {

    protected String description;

    protected Map<String, Map<String, Map<String, String>>> form;

    protected String status;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    protected String venue;
    protected String venueName;
    protected String venueAddress;
    protected String venueInstitute;

    protected EventModel() {
    }


    protected EventModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        creator = in.readString();
        creatorName = in.readString();
        creatorPhone = in.readString();
        creatorMail = in.readString();
        creatorProfilePicture = in.readString();
        creatorInstitute = in.readString();
        image = in.readString();
        status = in.readString();
        startTime = Instant.ofEpochSecond(in.readLong()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        endTime = Instant.ofEpochSecond(in.readLong()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        venue = in.readString();
        venueName = in.readString();
        venueAddress = in.readString();
        venueInstitute = in.readString();
        tags = ImmutableList.copyOf(in.createStringArrayList());
        timeCreated = Instant.ofEpochSecond(in.readLong()).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {
            return new EventModel(in);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public Map <String, Map<String, Map<String, String>>> getForm() {
        return form;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getVenue() {
        return venue;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public String getVenueInstitute() {
        return venueInstitute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(creator);
        dest.writeString(creatorName);
        dest.writeString(creatorPhone);
        dest.writeString(creatorMail);
        dest.writeString(creatorProfilePicture);
        dest.writeString(creatorInstitute);
        dest.writeString(image);
        dest.writeString(status);
        dest.writeLong(startTime.toEpochSecond(ZoneOffset.UTC));
        dest.writeLong(endTime.toEpochSecond(ZoneOffset.UTC));
        dest.writeString(venue);
        dest.writeString(venueName);
        dest.writeString(venueAddress);
        dest.writeString(venueInstitute);
        dest.writeStringList(tags);
        dest.writeLong(timeCreated.toEpochSecond(ZoneOffset.UTC));
    }
}