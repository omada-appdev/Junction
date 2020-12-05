package com.omada.junction.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.Map;

public class ArticleModelRemoteDB extends BaseModel{

    private String articleId;
    private String articleTitle;
    private String articlePublisher;
    private String articleText;
    private String articleAuthor;

    private Map<String, String> articlePublisherCache;

    private String articlePoster;

    @Exclude
    public String getArticleId() {
        return articleId;
    }

    @Exclude
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @PropertyName("articlePublisher")
    public String getArticlePublisher() {
        return articlePublisher;
    }

    @PropertyName("articlePublisher")
    public void setArticlePublisher(String articlePublisher) {
        this.articlePublisher = articlePublisher;
    }

    @PropertyName("articleText")
    public String getArticleText() {
        return articleText;
    }

    @PropertyName("articleText")
    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    @PropertyName("articleTitle")
    public String getArticleTitle() {
        return articleTitle;
    }

    @PropertyName("articleTitle")
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    @PropertyName("articleAuthor")
    public String getArticleAuthor() {
        return articleAuthor;
    }

    @PropertyName("articleAuthor")
    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    @PropertyName("articlePoster")
    public String getArticlePoster() {
        return articlePoster;
    }

    @PropertyName("articlePoster")
    public void setArticlePoster(String articlePoster) {
        this.articlePoster = articlePoster;
    }

    @PropertyName("articlePublisherCache")
    public Map<String, String> getArticlePublisherCache() {
        return articlePublisherCache;
    }

    @PropertyName("articlePublisherCache")
    public void setArticlePublisherCache(Map<String, String> articlePublisherCache) {
        this.articlePublisherCache = articlePublisherCache;
    }
}
