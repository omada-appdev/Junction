package com.omada.junction.data.models;

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

    @Exclude
    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    @NonNull
    @Override
    public String toString() {
        return interestString;
    }
}
