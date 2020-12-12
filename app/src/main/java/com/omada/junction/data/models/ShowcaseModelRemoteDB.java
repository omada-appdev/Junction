package com.omada.junction.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

public final class ShowcaseModelRemoteDB {

    private String showcaseID;
    private String showcaseTitle;

    private String creator;
    private String showcaseCreatorType;
    private String showcasePhoto;

    @Exclude
    public String getShowcaseID() {
        return showcaseID;
    }

    @Exclude
    public void setShowcaseID(String showcaseID) {
        this.showcaseID = showcaseID;
    }

    @PropertyName("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @PropertyName("creator")
    public String getCreator() {
        return creator;
    }

    @PropertyName("showcaseCreatorType")
    public void setShowcaseCreatorType(String showcaseCreatorType) {
        this.showcaseCreatorType = showcaseCreatorType;
    }

    @PropertyName("showcaseCreatorType")
    public String getShowcaseCreatorType() {
        return showcaseCreatorType;
    }

    @PropertyName("showcasePhoto")
    public void setShowcasePhoto(String showcasePhoto) {
        this.showcasePhoto = showcasePhoto;
    }

    @PropertyName("showcasePhoto")
    public String getShowcasePhoto() {
        return showcasePhoto;
    }

    @PropertyName("showcaseTitle")
    public void setShowcaseTitle(String showcaseTitle) {
        this.showcaseTitle = showcaseTitle;
    }

    @PropertyName("showcaseTitle")
    public String getShowcaseTitle() {
        return showcaseTitle;
    }
}
