package com.omada.junction.data.models.internal.remote;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.omada.junction.data.models.external.MemberModel;
import com.omada.junction.data.models.internal.BaseModelInternal;

public class MemberModelRemoteDB extends BaseModelInternal {

    private String user;
    private String name;
    private String photo;

    private String position;

    private Timestamp dateJoined;

    public MemberModelRemoteDB(){
    }

    public MemberModelRemoteDB(String id) {
        super(id);
    }

    @PropertyName("user")
    public String getUser() {
        return user;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("photo")
    public String getPhoto() {
        return photo;
    }

    @PropertyName("position")
    public String getPosition() {
        return position;
    }

    @PropertyName("dateJoined")
    public Timestamp getDateJoined() {
        return dateJoined;
    }



    @PropertyName("user")
    public void setUser(String user) {
        this.user = user;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @PropertyName("position")
    public void setPosition(String position) {
        this.position = position;
    }

    @PropertyName("dateJoined")
    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }
}
