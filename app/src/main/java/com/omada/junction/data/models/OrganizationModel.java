package com.omada.junction.data.models;

import java.util.List;

public class OrganizationModel extends BaseModel {

    private final String organizationID;
    private final String name;

    private final String profilePhoto;

    private final Integer attendedUsersNumber;
    private final Integer heldEventsNumber;
    private final String institute;

    private final List<String> interests;
    private final String mail;
    private final String phone;

    private final String type;

    //dummy model initializer
    public OrganizationModel(){

        organizationID = null;
        name = null;
        attendedUsersNumber = null;
        heldEventsNumber = null;
        institute = null;
        interests = null;
        mail = null;
        phone = null;
        type = null;
        profilePhoto =null;
    }

    public OrganizationModel(OrganizationModelRemoteDB modelRemote){
        attendedUsersNumber = modelRemote.getAttendedUsersNumber();
        heldEventsNumber = modelRemote.getHeldEventsNumber();
        institute = modelRemote.getInstitute();
        organizationID = modelRemote.getOrganizationID();
        name = modelRemote.getName();
        interests = modelRemote.getInterests();
        mail = modelRemote.getMail();
        phone = modelRemote.getPhone();
        type = modelRemote.getType();
        profilePhoto = modelRemote.getProfilePhoto();
    }

    public String getName() {
        return name;
    }

    public Integer getAttendedUsersNumber() {
        return attendedUsersNumber;
    }

    public Integer getHeldEventsNumber() {
        return heldEventsNumber;
    }

    public String getInstitute() {
        return institute;
    }

    public List<String> getInterests() {
        return interests;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }
}
