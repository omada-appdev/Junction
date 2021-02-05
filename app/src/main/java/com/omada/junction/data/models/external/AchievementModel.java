package com.omada.junction.data.models.external;

import android.os.Parcel;
import android.os.Parcelable;

public class AchievementModel extends BaseModel {

    protected AchievementModel(Parcel in) {
    }

    public static final Creator<AchievementModel> CREATOR = new Creator<AchievementModel>() {
        @Override
        public AchievementModel createFromParcel(Parcel in) {
            return new AchievementModel(in);
        }

        @Override
        public AchievementModel[] newArray(int size) {
            return new AchievementModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
