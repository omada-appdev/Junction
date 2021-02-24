package com.omada.junction.data.models.internal.remote;

import com.google.firebase.firestore.PropertyName;
import com.omada.junction.data.models.internal.BaseModelInternal;

public final class ShowcaseModelRemoteDB extends BaseModelInternal {

    private String title;

    private String creator;
    private String creatorType;
    private String photo;

    public ShowcaseModelRemoteDB(String id) {
        super(id);
    }

    public ShowcaseModelRemoteDB(){
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("creatorType")
    public String getCreatorType() {
        return creatorType;
    }

    @PropertyName("creator")
    public String getCreator() {
        return creator;
    }

    @PropertyName("photo")
    public String getPhoto() {
        return photo;
    }


    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("creatorType")
    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    @PropertyName("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @PropertyName("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
