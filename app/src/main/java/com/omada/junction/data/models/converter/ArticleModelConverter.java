package com.omada.junction.data.models.converter;

import com.google.common.collect.ImmutableList;
import com.omada.junction.data.models.external.ArticleModel;
import com.omada.junction.data.models.internal.remote.ArticleModelRemoteDB;
import com.omada.junction.data.models.mutable.MutableArticleModel;
import com.omada.junction.utils.TransformUtilities;

import java.util.HashMap;
import java.util.Map;

public class ArticleModelConverter extends BaseConverter <ArticleModel, ArticleModelRemoteDB, Void>{

    @Override
    public ArticleModel convertLocalDBToExternalModel(Void localModel) {
        return null;
    }

    @Override
    public ArticleModel convertRemoteDBToExternalModel(ArticleModelRemoteDB remoteModel) {

        MutableArticleModel model = new MutableArticleModel();

        model.setId(remoteModel.getId());
        model.setTitle(remoteModel.getTitle());
        model.setText(remoteModel.getText());
        model.setCreator(remoteModel.getCreator());
        model.setCreatorName(remoteModel.getCreatorCache().get("name"));
        model.setCreatorMail(remoteModel.getCreatorCache().get("profilePicture"));
        model.setCreatorPhone(remoteModel.getCreatorCache().get("profilePicture"));
        model.setCreatorProfilePicture(remoteModel.getCreatorCache().get("profilePicture"));
        model.setCreatorInstitute(remoteModel.getCreatorCache().get("institute"));
        model.setAuthor(remoteModel.getAuthor());
        model.setImage(remoteModel.getImage());
        model.setTimeCreated(TransformUtilities.convertTimestampToLocalDateTime(remoteModel.getTimeCreated()));
        model.setTags(ImmutableList.copyOf(remoteModel.getTags()));

        return model;
    }

    @Override
    public ArticleModelRemoteDB convertExternalToRemoteDBModel(ArticleModel externalModel) {
        ArticleModelRemoteDB modelRemoteDB = new ArticleModelRemoteDB();

        modelRemoteDB.setId(externalModel.getId());
        modelRemoteDB.setCreator(externalModel.getCreator());
        modelRemoteDB.setAuthor(externalModel.getAuthor());
        modelRemoteDB.setImage(externalModel.getImage());
        modelRemoteDB.setTitle(externalModel.getTitle());
        modelRemoteDB.setText(externalModel.getText());
        modelRemoteDB.setTimeCreated(TransformUtilities.convertUtcLocalDateTimeToTimestamp(externalModel.getTimeCreated()));

        Map<String, String> creatorCache = new HashMap<>();
        creatorCache.put("name", externalModel.getCreatorName());
        creatorCache.put("phone", externalModel.getCreatorPhone());
        creatorCache.put("mail", externalModel.getCreatorMail());
        creatorCache.put("profilePicture", externalModel.getCreatorProfilePicture());
        creatorCache.put("institute", externalModel.getCreatorInstitute());
        modelRemoteDB.setCreatorCache(creatorCache);

        modelRemoteDB.setTags(externalModel.getTags());

        return modelRemoteDB;
    }

    @Override
    public Void convertExternalToLocalDBModel(ArticleModel externalModel) {
        return null;
    }

    @Override
    public Map<String, Object> convertExternalToMapObject(ArticleModel externalModel) {
        return null;
    }

    @Override
    public ArticleModel convertMapObjectToExternal(Map<String, Object> externalModel) {
        return null;
    }
}
