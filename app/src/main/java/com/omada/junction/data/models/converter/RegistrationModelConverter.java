package com.omada.junction.data.models.converter;

import com.omada.junction.data.models.external.RegistrationModel;
import com.omada.junction.data.models.internal.remote.RegistrationModelRemoteDB;
import com.omada.junction.data.models.mutable.MutableRegistrationModel;

import java.util.Map;

public class RegistrationModelConverter extends BaseConverter<RegistrationModel, RegistrationModelRemoteDB, Void> {

    @Override
    public RegistrationModel convertLocalDBToExternalModel(Void localModel) {
        return null;
    }

    @Override
    public RegistrationModel convertRemoteDBToExternalModel(RegistrationModelRemoteDB remoteModel) {

        MutableRegistrationModel registrationModel = new MutableRegistrationModel();

        registrationModel.setUser(remoteModel.getUser());
        registrationModel.setUserProfilePicture(remoteModel.getUserProfilePicture());
        registrationModel.setUserInstitute(remoteModel.getUserInstitute());
        registrationModel.setUserMail(remoteModel.getUserMail());
        registrationModel.setUserPhone(remoteModel.getUserPhone());

        registrationModel.setResponses(registrationModel.getResponses());

        return registrationModel;
    }

    @Override
    public RegistrationModelRemoteDB convertExternalToRemoteDBModel(RegistrationModel externalModel) {

        RegistrationModelRemoteDB model = new RegistrationModelRemoteDB();

        model.setUser(externalModel.getUser());
        model.setUserProfilePicture(externalModel.getUserProfilePicture());
        model.setUserInstitute(externalModel.getUserInstitute());
        model.setUserMail(externalModel.getUserMail());
        model.setUserPhone(externalModel.getUserPhone());

        model.setResponses(externalModel.getResponses());

        return model;

    }

    @Override
    public Void convertExternalToLocalDBModel(RegistrationModel externalModel) {
        return null;
    }

    @Override
    public Map<String, Object> convertExternalToMapObject(RegistrationModel externalModel) {
        return null;
    }

    @Override
    public RegistrationModel convertMapObjectToExternal(Map<String, Object> externalModel) {
        return null;
    }
}
