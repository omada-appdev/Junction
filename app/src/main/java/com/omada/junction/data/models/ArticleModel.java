package com.omada.junction.data.models;

public class ArticleModel extends ArticleModelRemoteDB{

    private String articlePublisherProfilePictureRemote;
    private String articlePublisherName;

    public ArticleModel(ArticleModelRemoteDB modelRemoteDB) {

        setArticleId(modelRemoteDB.getArticleId());
        setArticleTitle(modelRemoteDB.getArticleTitle());
        setArticleText(modelRemoteDB.getArticleText());

        setArticlePublisher(modelRemoteDB.getArticlePublisher());
        setArticlePublisherName(modelRemoteDB.getArticlePublisherCache().get("name"));

        setArticlePublisherProfilePictureRemote(modelRemoteDB.getArticlePublisherCache().get("profilePicture"));
        setArticleAuthor(modelRemoteDB.getArticleAuthor());

        setArticlePoster(modelRemoteDB.getArticlePoster());
    }

    public void setArticlePublisherProfilePictureRemote(String articlePublisherProfilePictureRemote) {
        this.articlePublisherProfilePictureRemote = articlePublisherProfilePictureRemote;
    }

    public String getArticlePublisherProfilePictureRemote() {
        return articlePublisherProfilePictureRemote;
    }

    public String getArticlePublisherName() {
        return articlePublisherName;
    }

    public void setArticlePublisherName(String articlePublisherName) {
        this.articlePublisherName = articlePublisherName;
    }
}
