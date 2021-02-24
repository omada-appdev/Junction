package com.omada.junction.data.models.mutable;

import com.omada.junction.data.models.external.RegistrationModel;

import java.util.Map;

public class MutableRegistrationModel extends RegistrationModel {

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserInstitute(String userInstitute){
        this.userInstitute = userInstitute;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setResponses(Map<String, Map<String, Map<String, String>>> responses) {
        this.responses = responses;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

}
