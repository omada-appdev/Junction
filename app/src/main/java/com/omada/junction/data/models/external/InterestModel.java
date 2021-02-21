package com.omada.junction.data.models.external;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class InterestModel extends BaseModel {

    private final int drawableResourceId;
    public final String interestString;

    public InterestModel(String interestString){
        this.interestString = interestString;
        this.drawableResourceId = 0;
    }

    public InterestModel(String interestString, int drawableResouceId){
        this.interestString = interestString;
        this.drawableResourceId = drawableResouceId;
    }

    protected InterestModel(Parcel in) {
        drawableResourceId = in.readInt();
        interestString = in.readString();
    }

    public static final Creator<InterestModel> CREATOR = new Creator<InterestModel>() {
        @Override
        public InterestModel createFromParcel(Parcel in) {
            return new InterestModel(in);
        }

        @Override
        public InterestModel[] newArray(int size) {
            return new InterestModel[size];
        }
    };

    @Exclude
    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    @NonNull
    @Override
    public String toString() {
        return interestString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawableResourceId);
        dest.writeString(interestString);
    }
}