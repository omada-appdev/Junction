package com.omada.junction.data.models.mutable;

import com.google.firebase.Timestamp;
import com.omada.junction.data.models.external.MemberModel;

import java.time.LocalDateTime;
import java.util.Date;

public class MutableMemberModel extends MemberModel {

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
