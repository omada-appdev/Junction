package com.omada.junction.data.models.internal.remote;

import com.google.firebase.firestore.PropertyName;
import com.omada.junction.data.models.internal.BaseModelInternal;

import java.util.List;

public class OrganizationModelRemoteDB extends BaseModelInternal {

    private String name;

    private String profilePicture;

    private Integer attendedUsersNumber;
    private Integer heldEventsNumber;
    private String institute;

    private List<String> interests;
    private String mail;
    private String phone;

    private String type;

    public OrganizationModelRemoteDB(String id) {
        super(id);
    }

    public OrganizationModelRemoteDB(){
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("attendedUsersNumber")
    public Integer getAttendedUsersNumber() {
        return attendedUsersNumber;
    }

    @PropertyName("heldEventsNumber")
    public Integer getHeldEventsNumber() {
        return heldEventsNumber;
    }

    @PropertyName("institute")
    public String getInstitute() {
        return institute;
    }

    @PropertyName("interests")
    public List<String> getInterests() {
        return interests;
    }

    @PropertyName("mail")
    public String getMail() {
        return mail;
    }

    @PropertyName("phone")
    public String getPhone() {
        return phone;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    @PropertyName("profilePicture")
    public String getProfilePicture() {
        return profilePicture;
    }



    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("attendedUsersNumber")
    public void setAttendedUsersNumber(Integer attendedUsersNumber) {
        this.attendedUsersNumber = attendedUsersNumber;
    }

    @PropertyName("heldEventsNumber")
    public void setHeldEventsNumber(Integer heldEventsNumber) {
        this.heldEventsNumber = heldEventsNumber;
    }

    @PropertyName("institute")
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    @PropertyName("interests")
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    @PropertyName("mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    @PropertyName("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @PropertyName("type")
    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("profilePicture")
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
