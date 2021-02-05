package com.omada.junction.data.models.external;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

public class RegistrationModel extends BaseModel implements Parcelable {

    protected String userMail;
    protected String userPhone;
    protected String userInstitute;
    protected String user;
    protected LocalDateTime timeCreated;
    protected String userProfilePicture;

    protected Map<String, Map<String, Map <String, String>>> responses;

    protected RegistrationModel(){
    }

    protected RegistrationModel(Parcel in) {
        id = in.readString();
        userMail = in.readString();
        userPhone = in.readString();
        userInstitute = in.readString();
        user = in.readString();
        timeCreated = Instant.ofEpochSecond(in.readLong()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        try {
            responses = (Map<String, Map<String, Map<String, String>>>)in.readSerializable();
        }
        catch (ClassCastException e){
            Log.e("RegistrationModel", "Error parcelling registration");
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public String getUserInstitute() {
        return userInstitute;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public Map<String, Map<String, Map<String, String>>> getResponses() {
        return responses;
    }

    public static final Creator<RegistrationModel> CREATOR = new Creator<RegistrationModel>() {
        @Override
        public RegistrationModel createFromParcel(Parcel in) {
            return new RegistrationModel(in);
        }

        @Override
        public RegistrationModel[] newArray(int size) {
            return new RegistrationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userMail);
        dest.writeString(userPhone);
        dest.writeString(userInstitute);
        dest.writeString(user);
        dest.writeLong(timeCreated.toEpochSecond(ZoneOffset.UTC));
        dest.writeSerializable((Serializable)responses);
    }
}
