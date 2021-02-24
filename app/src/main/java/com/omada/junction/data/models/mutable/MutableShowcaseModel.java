package com.omada.junction.data.models.mutable;

import com.omada.junction.data.models.external.ShowcaseModel;

public class MutableShowcaseModel extends ShowcaseModel {

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
