package com.omada.junction.data.models.converter;

import com.omada.junction.data.models.external.NotificationModel;
import com.omada.junction.data.models.internal.remote.NotificationModelRemoteDB;
import com.omada.junction.data.models.mutable.MutableNotificationModel;

import java.util.Map;

public class NotificationModelConverter extends BaseConverter <NotificationModel, NotificationModelRemoteDB, Void> {

    @Override
    public NotificationModel convertLocalDBToExternalModel(Void localModel) {
        return null;
    }

    @Override
    public NotificationModel convertRemoteDBToExternalModel(NotificationModelRemoteDB remoteModel) {
        MutableNotificationModel model = new MutableNotificationModel();
        model.setId(remoteModel.getId());
        model.setNotificationType(remoteModel.getNotificationType());
        model.setSourceType(remoteModel.getSourceType());
        model.setSource(remoteModel.getSource());
        model.setTitle(remoteModel.getTitle());
        model.setText(remoteModel.getText());
        model.setStatus(remoteModel.getStatus());
        model.setData(remoteModel.getData());
        return model;
    }

    @Override
    public NotificationModelRemoteDB convertExternalToRemoteDBModel(NotificationModel externalModel) {
        NotificationModelRemoteDB model = new NotificationModelRemoteDB();
        model.setId(externalModel.getId());
        model.setNotificationType(externalModel.getNotificationType());
        model.setSourceType(externalModel.getSourceType());
        model.setSource(externalModel.getSource());
        model.setTitle(externalModel.getTitle());
        model.setText(externalModel.getText());
        model.setStatus(externalModel.getStatus());
        model.setData(externalModel.getData());
        return model;
    }

    @Override
    public Void convertExternalToLocalDBModel(NotificationModel externalModel) {
        return null;
    }

    @Override
    public Map<String, Object> convertExternalToMapObject(NotificationModel externalModel) {
        return null;
    }

    @Override
    public NotificationModel convertMapObjectToExternal(Map<String, Object> externalModel) {
        return null;
    }
}
