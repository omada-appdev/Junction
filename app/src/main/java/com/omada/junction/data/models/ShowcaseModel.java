package com.omada.junction.data.models;

public final class ShowcaseModel extends BaseModel{

    private String showcaseID = null;
    private String showcaseTitle = null;

    private String showcaseCreator = null;
    private String showcaseCreatorType = null;
    private String showcasePhoto = null;

    public ShowcaseModel(){
    }

    public ShowcaseModel(ShowcaseModelRemoteDB modelRemoteDB){

        showcaseID = modelRemoteDB.getShowcaseID();
        showcaseTitle = modelRemoteDB.getShowcaseTitle();
        showcaseCreator = modelRemoteDB.getShowcaseTitle();
        showcaseCreatorType = modelRemoteDB.getShowcaseCreatorType();
        showcasePhoto = modelRemoteDB.getShowcasePhoto();

    }

    public String getShowcaseID() {
        return showcaseID;
    }

    public String getShowcaseTitle() {
        return showcaseTitle;
    }

    public String getShowcaseCreator() {
        return showcaseCreator;
    }

    public String getShowcaseCreatorType() {
        return showcaseCreatorType;
    }

    public String getShowcasePhoto() {
        return showcasePhoto;
    }
}
