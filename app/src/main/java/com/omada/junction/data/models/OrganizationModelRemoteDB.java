package com.omada.junction.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class OrganizationModelRemoteDB extends BaseModel {

    private String organizationID;
    private String name;

    private String profilePhoto;

    private Integer attendedUsersNumber;
    private Integer heldEventsNumber;
    private String institute;

    private List<String> interests;
    private String mail;
    private String phone;

    private String type;

    @Exclude
    public String getOrganizationID() {
        return organizationID;
    }

    @Exclude
    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("attendedUsersNumber")
    public Integer getAttendedUsersNumber() {
        return attendedUsersNumber;
    }

    @PropertyName("attendedUsersNumber")
    public void setAttendedUsersNumber(Integer attendedUsersNumber) {
        this.attendedUsersNumber = attendedUsersNumber;
    }

    @PropertyName("heldEventsNumber")
    public Integer getHeldEventsNumber() {
        return heldEventsNumber;
    }

    @PropertyName("heldEventsNumber")
    public void setHeldEventsNumber(Integer heldEventsNumber) {
        this.heldEventsNumber = heldEventsNumber;
    }

    @PropertyName("institute")
    public String getInstitute() {
        return institute;
    }

    @PropertyName("institute")
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    @PropertyName("interests")
    public List<String> getInterests() {
        return interests;
    }

    @PropertyName("interests")
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    @PropertyName("mail")
    public String getMail() {
        return mail;
    }

    @PropertyName("mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    @PropertyName("phone")
    public String getPhone() {
        return phone;
    }

    @PropertyName("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    @PropertyName("type")
    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("profilePicture")
    public String getProfilePhoto() {
        return profilePhoto;
    }

    @PropertyName("profilePicture")
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
