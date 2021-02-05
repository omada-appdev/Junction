package com.omada.junction.data.models.external;

import android.os.Parcel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


public class MemberModel extends BaseModel {

    protected String user;
    protected String name;
    protected String photo;

    protected LocalDateTime dateJoined;

    protected String position;

    protected MemberModel(){
    }

    protected MemberModel(Parcel in) {
        id = in.readString();
        user = in.readString();
        name = in.readString();
        photo = in.readString();
        dateJoined = Instant.ofEpochSecond(in.readLong()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        position = in.readString();
    }

    public static final Creator<MemberModel> CREATOR = new Creator<MemberModel>() {
        @Override
        public MemberModel createFromParcel(Parcel in) {
            return new MemberModel(in);
        }

        @Override
        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public String getPosition() {
        return position;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeLong(dateJoined.toEpochSecond(ZoneOffset.UTC));
        dest.writeString(position);
    }
}
