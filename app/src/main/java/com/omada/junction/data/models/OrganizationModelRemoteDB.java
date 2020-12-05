package com.omada.junction.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.omada.junction.data.models.BaseModel;

import java.util.List;

public class OrganizationModelRemoteDB extends BaseModel {

    private String organizationID;
    private String organizationName;

    private String organizationProfilePhoto;

    private String attendedUsersNumber;
    private String heldEventsNumber;
    private String organizationInstitute;

    private List<String> organizationInterests;
    private String organizationMail;
    private String organizationPhone;

    private String organizationType;

    @Exclude
    public String getOrganizationID() {
        return organizationID;
    }

    @Exclude
    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    @PropertyName("organizationName")
    public String getOrganizationName() {
        return organizationName;
    }

    @PropertyName("organizationName")
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @PropertyName("attendedUsersNumber")
    public String getAttendedUsersNumber() {
        return attendedUsersNumber;
    }

    @PropertyName("attendedUsersNumber")
    public void setAttendedUsersNumber(String attendedUsersNumber) {
        this.attendedUsersNumber = attendedUsersNumber;
    }

    @PropertyName("heldEventsNumber")
    public String getHeldEventsNumber() {
        return heldEventsNumber;
    }

    @PropertyName("heldEventsNumber")
    public void setHeldEventsNumber(String heldEventsNumber) {
        this.heldEventsNumber = heldEventsNumber;
    }

    @PropertyName("organizationInstitute")
    public String getOrganizationInstitute() {
        return organizationInstitute;
    }

    @PropertyName("organizationInstitute")
    public void setOrganizationInstitute(String organizationInstitute) {
        this.organizationInstitute = organizationInstitute;
    }

    @PropertyName("organizationInterests")
    public List<String> getOrganizationInterests() {
        return organizationInterests;
    }

    @PropertyName("organizationInterests")
    public void setOrganizationInterests(List<String> organizationInterests) {
        this.organizationInterests = organizationInterests;
    }

    @PropertyName("organizationMail")
    public String getOrganizationMail() {
        return organizationMail;
    }

    @PropertyName("organizationMail")
    public void setOrganizationMail(String organizationMail) {
        this.organizationMail = organizationMail;
    }

    @PropertyName("organizationPhone")
    public String getOrganizationPhone() {
        return organizationPhone;
    }

    @PropertyName("organizationPhone")
    public void setOrganizationPhone(String organizationPhone) {
        this.organizationPhone = organizationPhone;
    }

    @PropertyName("organizationType")
    public String getOrganizationType() {
        return organizationType;
    }

    @PropertyName("organizationType")
    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    @PropertyName("organizationProfilePicture")
    public String getOrganizationProfilePhoto() {
        return organizationProfilePhoto;
    }

    @PropertyName("organizationProfilePicture")
    public void setOrganizationProfilePhoto(String organizationProfilePhoto) {
        this.organizationProfilePhoto = organizationProfilePhoto;
    }
}
