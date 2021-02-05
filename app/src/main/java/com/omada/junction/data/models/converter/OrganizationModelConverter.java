package com.omada.junction.data.models.converter;

import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.data.models.internal.remote.OrganizationModelRemoteDB;
import com.omada.junction.data.models.mutable.MutableOrganizationModel;

import java.util.Map;

public class OrganizationModelConverter extends BaseConverter <OrganizationModel, OrganizationModelRemoteDB, Void>{

    @Override
    public OrganizationModel convertLocalDBToExternalModel(Void localModel) {
        return null;
    }

    @Override
    public OrganizationModel convertRemoteDBToExternalModel(OrganizationModelRemoteDB remoteModel) {

        MutableOrganizationModel model = new MutableOrganizationModel();

        model.setId(remoteModel.getId());
        model.setType(remoteModel.getType());
        model.setAttendedUsersNumber(remoteModel.getAttendedUsersNumber());
        model.setHeldEventsNumber(remoteModel.getHeldEventsNumber());
        model.setInstitute(remoteModel.getInstitute());
        model.setInterests(remoteModel.getInterests());
        model.setMail(remoteModel.getMail());
        model.setName(remoteModel.getName());
        model.setPhone(remoteModel.getPhone());
        model.setProfilePicture(remoteModel.getProfilePicture());

        return model;

    }

    @Override
    public OrganizationModelRemoteDB convertExternalToRemoteDBModel(OrganizationModel externalModel) {

        OrganizationModelRemoteDB model = new OrganizationModelRemoteDB();

        model.setId(externalModel.getId());

        model.setType(externalModel.getType());
        model.setInstitute(externalModel.getInstitute());

        model.setName(externalModel.getName());
        model.setMail(externalModel.getMail());
        model.setPhone(externalModel.getPhone());
        model.setProfilePicture(externalModel.getProfilePicture());

        model.setInterests(externalModel.getInterests());
        model.setAttendedUsersNumber(externalModel.getAttendedUsersNumber());
        model.setHeldEventsNumber(externalModel.getHeldEventsNumber());

        return model;
    }

    @Override
    public Void convertExternalToLocalDBModel(OrganizationModel externalModel) {
        return null;
    }

    @Override
    public Map<String, Object> convertExternalToMapObject(OrganizationModel externalModel) {
        return null;
    }

    @Override
    public OrganizationModel convertMapObjectToExternal(Map<String, Object> externalModel) {
        return null;
    }
}
