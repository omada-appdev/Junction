package com.omada.junction.data.models;

import java.util.List;

public class OrganizationModel extends BaseModel {

    private final String organizationID;
    private final String organizationName;

    private final String organizationProfilePhoto;

    private final String attendedUsersNumber;
    private final String heldEventsNumber;
    private final String organizationInstitute;

    private final List<String> organizationInterests;
    private final String organizationMail;
    private final String organizationPhone;

    private final String organizationType;

    //dummy model initializer
    public OrganizationModel(){

        organizationID = null;
        organizationName = null;
        attendedUsersNumber = null;
        heldEventsNumber = null;
        organizationInstitute = null;
        organizationInterests = null;
        organizationMail = null;
        organizationPhone = null;
        organizationType = null;
        organizationProfilePhoto =null;
    }

    public OrganizationModel(OrganizationModelRemoteDB modelRemote){
        attendedUsersNumber = modelRemote.getAttendedUsersNumber();
        heldEventsNumber = modelRemote.getHeldEventsNumber();
        organizationInstitute = modelRemote.getOrganizationInstitute();
        organizationID = modelRemote.getOrganizationID();
        organizationName = modelRemote.getOrganizationName();
        organizationInterests = modelRemote.getOrganizationInterests();
        organizationMail = modelRemote.getOrganizationMail();
        organizationPhone = modelRemote.getOrganizationPhone();
        organizationType = modelRemote.getOrganizationType();
        organizationProfilePhoto = modelRemote.getOrganizationProfilePhoto();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getAttendedUsersNumber() {
        return attendedUsersNumber;
    }

    public String getHeldEventsNumber() {
        return heldEventsNumber;
    }

    public String getOrganizationInstitute() {
        return organizationInstitute;
    }

    public List<String> getOrganizationInterests() {
        return organizationInterests;
    }

    public String getOrganizationMail() {
        return organizationMail;
    }

    public String getOrganizationPhone() {
        return organizationPhone;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public String getOrganizationProfilePhoto() {
        return organizationProfilePhoto;
    }
}
