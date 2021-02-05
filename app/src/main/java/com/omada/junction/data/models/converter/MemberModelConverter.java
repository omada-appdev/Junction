package com.omada.junction.data.models.converter;

import com.google.firebase.Timestamp;
import com.omada.junction.data.models.external.MemberModel;
import com.omada.junction.data.models.internal.remote.MemberModelRemoteDB;
import com.omada.junction.data.models.mutable.MutableMemberModel;
import com.omada.junction.utils.TransformUtilities;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;


public class MemberModelConverter extends BaseConverter <MemberModel, MemberModelRemoteDB, Void> {

    @Override
    public MemberModel convertLocalDBToExternalModel(Void localModel) {
        return null;
    }

    @Override
    public MemberModel convertRemoteDBToExternalModel(MemberModelRemoteDB remoteModel) {

        MutableMemberModel model = new MutableMemberModel();
        
        model.setId(remoteModel.getId());

        model.setUser(remoteModel.getUser());
        model.setName(remoteModel.getName());
        model.setPhoto(remoteModel.getPhoto());

        model.setDateJoined(TransformUtilities.convertTimestampToLocalDateTime(remoteModel.getDateJoined()));
        model.setPosition(remoteModel.getPosition());

        return model;
        
    }

    @Override
    public MemberModelRemoteDB convertExternalToRemoteDBModel(MemberModel externalModel) {

        MemberModelRemoteDB model = new MemberModelRemoteDB();

        model.setId(externalModel.getId());
        model.setName(externalModel.getName());
        model.setPhoto(externalModel.getPhoto());
        model.setDateJoined(new Timestamp(Date.from(externalModel.getDateJoined().toInstant(ZoneOffset.UTC))));
        model.setPosition(externalModel.getPosition());
        model.setUser(externalModel.getUser());

        return model;

    }

    @Override
    public Void convertExternalToLocalDBModel(MemberModel externalModel) {
        return null;
    }

    @Override
    public Map<String, Object> convertExternalToMapObject(MemberModel externalModel) {
        return null;
    }

    @Override
    public MemberModel convertMapObjectToExternal(Map<String, Object> externalModel) {
        return null;
    }
}
