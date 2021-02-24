package com.omada.junction.data.models.external;

import com.google.common.collect.ImmutableList;

import java.time.LocalDateTime;
import java.util.ArrayList;


public abstract class PostModel extends BaseModel {

    protected String type;

    protected String title;
    protected String image;

    protected LocalDateTime timeCreated;

    protected String creator;
    protected String creatorName;
    protected String creatorProfilePicture;
    protected String creatorMail;
    protected String creatorPhone;
    protected String creatorInstitute;

    protected ImmutableList<String> tags = ImmutableList.copyOf(new ArrayList<>());

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorPhone() {
        return creatorPhone;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public String getCreatorProfilePicture() {
        return creatorProfilePicture;
    }

    public String getCreatorInstitute() {
        return creatorInstitute;
    }

    public ImmutableList<String> getTags() {
        return tags;
    }
}
