package com.omada.junction.data.models.converter;

import java.util.Map;

public abstract class BaseConverter <E, R, L> {

    public abstract E convertLocalDBToExternalModel(L localModel);
    public abstract E convertRemoteDBToExternalModel(R remoteModel);
    public abstract R convertExternalToRemoteDBModel(E externalModel);
    public abstract L convertExternalToLocalDBModel(E externalModel);
    public abstract Map<String, Object> convertExternalToMapObject(E externalModel);
    public abstract E convertMapObjectToExternal(Map<String, Object> externalModel);

}
