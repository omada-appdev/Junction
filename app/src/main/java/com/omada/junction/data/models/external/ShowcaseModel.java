package com.omada.junction.data.models.external;

import android.os.Parcel;
import android.os.Parcelable;

import com.omada.junction.data.models.internal.remote.ShowcaseModelRemoteDB;

public class ShowcaseModel extends BaseModel {

    protected String title;

    protected String creator;
    protected String creatorType;
    protected String photo;

    public ShowcaseModel(){
    }

    protected ShowcaseModel(Parcel in) {
        title = in.readString();
        creator = in.readString();
        creatorType = in.readString();
        photo = in.readString();
    }

    public static final Creator<ShowcaseModel> CREATOR = new Creator<ShowcaseModel>() {
        @Override
        public ShowcaseModel createFromParcel(Parcel in) {
            return new ShowcaseModel(in);
        }

        @Override
        public ShowcaseModel[] newArray(int size) {
            return new ShowcaseModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorType() {
        return creatorType;
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(creator);
        dest.writeString(creatorType);
        dest.writeString(photo);
    }

}
