package com.omada.junction.data.models;

public class ArticleModel extends ArticleModelRemoteDB{

    private String creatorProfilePicture;
    private String creatorName;

    public ArticleModel(ArticleModelRemoteDB modelRemoteDB) {

        setId(modelRemoteDB.getId());
        setTitle(modelRemoteDB.getTitle());
        setText(modelRemoteDB.getText());

        setCreator(modelRemoteDB.getCreator());
        setCreatorName(modelRemoteDB.getCreatorCache().get("name"));

        setCreatorProfilePicture(modelRemoteDB.getCreatorCache().get("profilePicture"));
        setAuthor(modelRemoteDB.getAuthor());

        setImage(modelRemoteDB.getImage());
    }

    public void setCreatorProfilePicture(String creatorProfilePicture) {
        this.creatorProfilePicture = creatorProfilePicture;
    }

    public String getCreatorProfilePicture() {
        return creatorProfilePicture;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
