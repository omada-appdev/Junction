package com.omada.junction.data.models.internal.remote;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;
import com.omada.junction.data.models.internal.BaseModelInternal;

import java.util.Date;
import java.util.Map;

public class RegistrationModelRemoteDB extends BaseModelInternal {

    private String userMail;
    private String userPhone;
    private String userInstitute;
    private String userProfilePicture;
    private String user;
    private Timestamp timeCreated;

    private Map<String, Map<String, Map<String, String>>> responses;

    public RegistrationModelRemoteDB(){

    }

    @PropertyName("mail")
    public String getUserMail() {
        return userMail;
    }

    @PropertyName("mail")
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @PropertyName("phone")
    public String getUserPhone() {
        return userPhone;
    }

    @PropertyName("phone")
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @PropertyName("institute")
    public String getUserInstitute() {
        return userInstitute;
    }

    @PropertyName("institute")
    public void setUserInstitute(String userInstitute) {
        this.userInstitute = userInstitute;
    }

    @PropertyName("user")
    public String getUser() {
        return user;
    }

    @PropertyName("user")
    public void setUser(String user) {
        this.user = user;
    }

    @ServerTimestamp
    @PropertyName("timeCreated")
    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    @PropertyName("timeCreated")
    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @PropertyName("responses")
    public Map<String, Map<String, Map<String, String>>> getResponses() {
        return responses;
    }

    @PropertyName("responses")
    public void setResponses(Map<String, Map<String, Map<String, String>>> responses) {
        this.responses = responses;
    }

    @PropertyName("profilePicture")
    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    @PropertyName("profilePicture")
    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }
}
