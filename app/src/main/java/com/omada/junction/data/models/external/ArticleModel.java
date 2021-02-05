package com.omada.junction.data.models.external;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.ImmutableList;
import com.google.firebase.Timestamp;
import com.omada.junction.data.models.internal.remote.ArticleModelRemoteDB;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

public class ArticleModel extends PostModel {

    protected String text;
    protected String author;

    protected ArticleModel(){
    }

    protected ArticleModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        creator = in.readString();
        text = in.readString();
        author = in.readString();
        creatorName = in.readString();
        creatorPhone = in.readString();
        creatorProfilePicture = in.readString();
        creatorMail = in.readString();
        creatorInstitute = in.readString();
        image = in.readString();
        tags = ImmutableList.copyOf(in.createStringArrayList());
        timeCreated = Instant.ofEpochSecond(in.readLong()).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(creator);
        dest.writeString(text);
        dest.writeString(author);
        dest.writeString(creatorName);
        dest.writeString(creatorPhone);
        dest.writeString(creatorProfilePicture);
        dest.writeString(creatorMail);
        dest.writeString(creatorInstitute);
        dest.writeString(image);
        dest.writeStringList(tags);
        dest.writeLong(timeCreated.toEpochSecond(ZoneOffset.UTC));
    }
}